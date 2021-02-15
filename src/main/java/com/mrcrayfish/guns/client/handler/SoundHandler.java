package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.audio.StunRingingSound;
import com.mrcrayfish.guns.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class SoundHandler
{
    private static SoundHandler instance;

    public static SoundHandler get()
    {
        if(instance == null)
        {
            instance = new SoundHandler();
        }
        return instance;
    }

    private final Map<ISound, Float> soundVolumes = new ConcurrentHashMap<>();
    private boolean isDeafened;
    private Field playingSounds;
    private SoundEngine soundEngine;
    private StunRingingSound ringing;

    private SoundHandler()
    {
        this.initReflection();
    }

    private void initReflection()
    {
        this.playingSounds = ObfuscationReflectionHelper.findField(SoundEngine.class, "field_217942_m");
    }

    @SubscribeEvent
    public void deafenPlayer(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START || Minecraft.getInstance().player == null || this.soundEngine == null)
        {
            return;
        }

        /* If deafened, play ringing sound if not already playing, otherwise return */
        EffectInstance effect = Minecraft.getInstance().player.getActivePotionEffect(ModEffects.DEAFENED.get());
        if(effect == null)
        {
            if(!this.isDeafened)
            {
                return;
            }
        }

        if(Config.SERVER.ringVolume.get() > 0 && (this.ringing == null || !Minecraft.getInstance().getSoundHandler().isPlaying(this.ringing)))
        {
            this.ringing = new StunRingingSound();
            Minecraft.getInstance().getSoundHandler().play(this.ringing);
            return; // Return after playing sound, as doing so in the tame tick that sounds are muted causes crashing in SoundManager#updateAllSounds
        }

        // Access the sound manager's sound system and list of playing sounds
        Map<ISound, ChannelManager.Entry> playingSounds;
        try
        {
            playingSounds = (Map<ISound, ChannelManager.Entry>) this.playingSounds.get(this.soundEngine);
        }
        catch(IllegalArgumentException | IllegalAccessException e)
        {
            return;
        }

        if(effect != null)
        {
            try
            {
                playingSounds.forEach((sound, entry) ->
                {
                    /* Exempt tickable sounds and stun grenade explosions from per-tick muting */
                    if(sound == null || sound instanceof ITickableSound || isStunGrenade(sound.getSound().getSoundLocation()))
                    {
                        return;
                    }

                    float volume = sound instanceof SoundMuted ? ((SoundMuted) sound).getVolumeInitial() : sound.getVolume();
                    this.soundVolumes.put(sound, volume);
                    entry.runOnSoundExecutor(soundSource ->
                    {
                        soundSource.setGain(getMutedVolume(effect.getDuration(), volume));
                    });
                });
            }
            catch(ConcurrentModificationException ignored) {}
            //SoundManager#playingSounds is accessed from another thread, so it's key set iterator can throw a CME
            this.isDeafened = true;
        }
        else if(this.isDeafened)
        {
            // Restore sound levels to initial values
            this.isDeafened = false;
            for(Entry<ISound, Float> entry : this.soundVolumes.entrySet())
            {
                ChannelManager.Entry entry1 = playingSounds.get(entry.getKey());
                if(entry1 != null)
                {
                    entry1.runOnSoundExecutor(soundSource -> soundSource.setGain(entry.getValue()));
                }
            }
            this.soundVolumes.clear();
        }

    }

    @SubscribeEvent
    public void lowerInitialVolume(PlaySoundEvent event)
    {
        if(this.soundEngine == null)
        {
            this.soundEngine = event.getManager();
        }

        if(!this.isDeafened || Minecraft.getInstance().player == null || event.getSound() instanceof ITickableSound)
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

    private boolean isStunGrenade(ResourceLocation loc)
    {
        return loc.toString().equals(Reference.MOD_ID + ":grenade_stun_explosion");
    }

    private float getMutedVolume(float duration, float volumeBase)
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
            this.volume = SoundHandler.get().getMutedVolume(duration, this.volumeInitial);
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
        public SoundEventAccessor createAccessor(net.minecraft.client.audio.SoundHandler handler)
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