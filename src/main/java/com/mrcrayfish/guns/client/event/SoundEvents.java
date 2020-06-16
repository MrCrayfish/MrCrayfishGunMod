package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.audio.StunRingingSound;
import com.mrcrayfish.guns.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class SoundEvents
{
    private static final Map<ISound, Float> SOUND_VOLUMES = new ConcurrentHashMap<>();
    private static boolean isDeafened;
    private static Field soundSystem, playingSounds;
    private static SoundEngine soundEngine;
    private static StunRingingSound ringing;

    public static void initReflection()
    {
        soundSystem = ObfuscationReflectionHelper.findField(SoundEngine.class, "field_148622_c");
        playingSounds = ObfuscationReflectionHelper.findField(SoundEngine.class, "field_217942_m");
    }

    @SubscribeEvent
    public static void deafenPlayer(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START || Minecraft.getInstance().player == null || soundEngine == null)
        {
            return;
        }

        /* If deafened, play ringing sound if not already playing, otherwise return */
        EffectInstance effect = Minecraft.getInstance().player.getActivePotionEffect(ModEffects.DEAFENED.get());
        if(effect == null)
        {
            if(!isDeafened)
            {
                return;
            }
        }

        if(Config.SERVER.ringVolume.get() > 0 && (ringing == null || !Minecraft.getInstance().getSoundHandler().isPlaying(ringing)))
        {
            ringing = new StunRingingSound();
            Minecraft.getInstance().getSoundHandler().play(ringing);
            return; // Return after playing sound, as doing so in the tame tick that sounds are muted causes crashing in SoundManager#updateAllSounds
        }

        // Access the sound manager's sound system and list of playing sounds
        Map<ISound, ChannelManager.Entry> playingSounds;
        try
        {
            playingSounds = (Map<ISound, ChannelManager.Entry>) SoundEvents.playingSounds.get(soundEngine);
        }
        catch(IllegalArgumentException | IllegalAccessException e)
        {
            return;
        }

        if(effect != null)
        {
            try
            {
                playingSounds.forEach((sound, entry) -> {
                    /* Exempt tickable sounds and stun grenade explosions from per-tick muting */
                    if(sound == null || sound instanceof ITickableSound || isStunGrenade(sound.getSound().getSoundLocation()))
                    {
                        return;
                    }

                    float volume = sound instanceof SoundMuted ? ((SoundMuted) sound).getVolumeInitial() : sound.getVolume();
                    SOUND_VOLUMES.put(sound, volume);

                    entry.runOnSoundExecutor(soundSource -> {
                        soundSource.func_216423_c(getMutedVolume(effect.getDuration(), volume));
                    });
                });
            }
            catch(ConcurrentModificationException ignored)
            {
            }
            //SoundManager#playingSounds is accessed from another thread, so it's key set iterator can throw a CME
            isDeafened = true;
        }
        else if(isDeafened)
        {
            // Restore sound levels to initial values
            isDeafened = false;
            for(Entry<ISound, Float> entry : SOUND_VOLUMES.entrySet())
            {
                ChannelManager.Entry entry1 = playingSounds.get(entry.getKey());
                if(entry1 != null)
                {
                    entry1.runOnSoundExecutor(soundSource -> {
                        soundSource.func_216423_c(entry.getValue());
                    });
                }
            }
            SOUND_VOLUMES.clear();
        }

    }

    @SubscribeEvent
    public static void lowerInitialVolume(PlaySoundEvent event)
    {
        if(soundEngine == null)
        {
            soundEngine = event.getManager();
        }

        if(!isDeafened || Minecraft.getInstance().player == null || event.getSound() instanceof ITickableSound)
        {
            return;
        }

        // Exempt initial explosion from muting
        ResourceLocation loc = event.getSound().getSoundLocation();
        EffectInstance effect = Minecraft.getInstance().player.getActivePotionEffect(ModEffects.DEAFENED.get());
        int duration = effect != null ? effect.getDuration() : 0;
        boolean isStunGrenade = isStunGrenade(loc);
        if(duration == 0 && isStunGrenade) return;

        // Reduce volume to full value when duration is above threshold
        // When below threshold, fade to original sound level as duration approaches 0
        event.getSound().createAccessor(Minecraft.getInstance().getSoundHandler());
        event.setResultSound(new SoundMuted(event.getSound(), duration, isStunGrenade));
    }

    private static boolean isStunGrenade(ResourceLocation loc)
    {
        return loc.toString().equals(Reference.MOD_ID + ":grenade_stun_explosion");
    }

    private static float getMutedVolume(float duration, float volumeBase)
    {
        float volumeMin = (float) (volumeBase * Config.SERVER.soundPercentage.get());
        float percent = Math.min((duration / Config.SERVER.soundFadeThreshold.get()), 1);
        return volumeMin + (1 - percent) * (volumeBase - volumeMin);
    }

    public static class SoundMuted implements ISound
    {
        private ISound parent;
        private float volume, volumeInitial;

        public SoundMuted(ISound parent, int duration, boolean isStunGrenade)
        {
            this.parent = parent;
            this.volumeInitial = MathHelper.clamp(parent.getVolume(), 0, 1);
            this.volume = SoundEvents.getMutedVolume(duration, this.volumeInitial);
            if(isStunGrenade)
            {
                this.volumeInitial = this.volume;
            }
        }

        @Override
        public float getVolume()
        {
            return this.volume;
        }

        public float getVolumeInitial()
        {
            return this.volumeInitial;
        }

        @Override
        public ResourceLocation getSoundLocation()
        {
            return this.parent.getSoundLocation();
        }

        @Override
        @Nullable
        public SoundEventAccessor createAccessor(SoundHandler handler)
        {
            return this.parent.createAccessor(handler);
        }

        @Override
        public Sound getSound()
        {
            return this.parent.getSound();
        }

        @Override
        public SoundCategory getCategory()
        {
            return this.parent.getCategory();
        }

        @Override
        public boolean canRepeat()
        {
            return this.parent.canRepeat();
        }

        @Override
        public boolean isGlobal()
        {
            return false;
        }

        @Override
        public int getRepeatDelay()
        {
            return this.parent.getRepeatDelay();
        }

        @Override
        public float getPitch()
        {
            return this.parent.getPitch();
        }

        @Override
        public float getX()
        {
            return this.parent.getX();
        }

        @Override
        public float getY()
        {
            return this.parent.getY();
        }

        @Override
        public float getZ()
        {
            return this.parent.getZ();
        }

        @Override
        public ISound.AttenuationType getAttenuationType()
        {
            return parent.getAttenuationType();
        }
    }
}