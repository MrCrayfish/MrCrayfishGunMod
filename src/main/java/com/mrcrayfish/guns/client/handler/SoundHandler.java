package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.audio.StunRingingSound;
import com.mrcrayfish.guns.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

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

    private final Map<SoundInstance, Float> soundVolumes = new ConcurrentHashMap<>();
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
        this.playingSounds = ObfuscationReflectionHelper.findField(SoundEngine.class, "f_120226_");
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void deafenPlayer(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START || Minecraft.getInstance().player == null || this.soundEngine == null)
        {
            return;
        }

        /* If deafened, play ringing sound if not already playing, otherwise return */
        MobEffectInstance effect = Minecraft.getInstance().player.getEffect(ModEffects.DEAFENED.get());
        if(effect == null)
        {
            if(!this.isDeafened)
            {
                return;
            }
        }

        if(Config.SERVER.ringVolume.get() > 0 && (this.ringing == null || !Minecraft.getInstance().getSoundManager().isActive(this.ringing)))
        {
            this.ringing = new StunRingingSound();
            Minecraft.getInstance().getSoundManager().play(this.ringing);
            return; // Return after playing sound, as doing so in the tame tick that sounds are muted causes crashing in SoundManager#updateAllSounds
        }

        // Access the sound manager's sound system and list of playing sounds
        Map<SoundInstance, ChannelAccess.ChannelHandle> playingSounds;
        try
        {
            playingSounds = (Map<SoundInstance, ChannelAccess.ChannelHandle>) this.playingSounds.get(this.soundEngine);
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
                    if(sound == null || sound instanceof TickableSoundInstance || isStunGrenade(sound.getSound().getLocation()))
                    {
                        return;
                    }

                    float volume = sound instanceof SoundMuted ? ((SoundMuted) sound).getVolumeInitial() : sound.getVolume();
                    this.soundVolumes.put(sound, volume);
                    entry.execute(soundSource ->
                    {
                        soundSource.setVolume(getMutedVolume(effect.getDuration(), volume));
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
            for(Entry<SoundInstance, Float> entry : this.soundVolumes.entrySet())
            {
                ChannelAccess.ChannelHandle entry1 = playingSounds.get(entry.getKey());
                if(entry1 != null)
                {
                    entry1.execute(soundSource -> soundSource.setVolume(entry.getValue()));
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
            this.soundEngine = event.getEngine();
        }

        if(!this.isDeafened || Minecraft.getInstance().player == null || event.getSound() instanceof TickableSoundInstance)
        {
            return;
        }

        // Exempt initial explosion from muting
        ResourceLocation loc = event.getSound().getLocation();
        MobEffectInstance effect = Minecraft.getInstance().player.getEffect(ModEffects.DEAFENED.get());
        int duration = effect != null ? effect.getDuration() : 0;
        boolean isStunGrenade = isStunGrenade(loc);
        if(duration == 0 && isStunGrenade) return;

        // Reduce volume to full value when duration is above threshold
        // When below threshold, fade to original sound level as duration approaches 0
        event.getSound().resolve(Minecraft.getInstance().getSoundManager());
        event.setSound(new SoundMuted(event.getSound(), duration, isStunGrenade));
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

    public static class SoundMuted implements SoundInstance
    {
        private SoundInstance parent;
        private float volume, volumeInitial;

        public SoundMuted(SoundInstance parent, int duration, boolean isStunGrenade)
        {
            this.parent = parent;
            this.volumeInitial = Mth.clamp(parent.getVolume(), 0, 1);
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
        public ResourceLocation getLocation()
        {
            return this.parent.getLocation();
        }

        @Override
        @Nullable
        public WeighedSoundEvents resolve(net.minecraft.client.sounds.SoundManager handler)
        {
            return this.parent.resolve(handler);
        }

        @Override
        public Sound getSound()
        {
            return this.parent.getSound();
        }

        @Override
        public SoundSource getSource()
        {
            return this.parent.getSource();
        }

        @Override
        public boolean isLooping()
        {
            return this.parent.isLooping();
        }

        @Override
        public boolean isRelative()
        {
            return false;
        }

        @Override
        public int getDelay()
        {
            return this.parent.getDelay();
        }

        @Override
        public float getPitch()
        {
            return this.parent.getPitch();
        }

        @Override
        public double getX()
        {
            return this.parent.getX();
        }

        @Override
        public double getY()
        {
            return this.parent.getY();
        }

        @Override
        public double getZ()
        {
            return this.parent.getZ();
        }

        @Override
        public SoundInstance.Attenuation getAttenuation()
        {
            return parent.getAttenuation();
        }
    }
}