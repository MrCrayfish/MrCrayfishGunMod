package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.audio.SoundRinging;
import com.mrcrayfish.guns.init.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import paulscode.sound.SoundSystem;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(Side.CLIENT)
public class SoundEvents
{
    private static final Map<String, Float> SOUND_VOLUMES = new ConcurrentHashMap<>();
    private static boolean isDeafened;
    private static Field soundSystem, playingSounds;
    private static SoundManager soundManager;
    private static SoundRinging ringing;

    public static void initReflection()
    {
        soundSystem = ReflectionHelper.findField(SoundManager.class, ObfuscationReflectionHelper.remapFieldNames(SoundManager.class.getName(), "field_148620_e"));
        playingSounds = ReflectionHelper.findField(SoundManager.class, ObfuscationReflectionHelper.remapFieldNames(SoundManager.class.getName(), "field_148629_h"));
    }

    @SubscribeEvent
    public static void deafenPlayer(ClientTickEvent event)
    {
        if (event.phase == Phase.START || Minecraft.getMinecraft().player == null || soundManager == null)
            return;

        // If deafened, play ringing sound if not already playing, otherwise return
        PotionEffect effect = Minecraft.getMinecraft().player.getActivePotionEffect(ModPotions.DEAFENED);
        if (effect == null)
        {
            if (!isDeafened)
                return;
        }
        else
        {
            isDeafened = true;
            if (GunConfig.SERVER.stunGrenades.deafen.ringVolumeSynced > 0
                    && (ringing == null || !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(ringing)))
            {
                ringing = new SoundRinging();
                Minecraft.getMinecraft().getSoundHandler().playSound(ringing);
                return; // Return after playing sound, as doing so in the tame tick that sounds are muted causes crashing in SoundManager#updateAllSounds
            }
        }

        // Access the sound manager's sound system and list of playing sounds
        SoundSystem soundSystem;
        Map<String, ISound> playingSounds;
        try
        {
            soundSystem = (SoundSystem) SoundEvents.soundSystem.get(soundManager);
            playingSounds = (Map<String, ISound>) SoundEvents.playingSounds.get(soundManager);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            return;
        }

        if (effect != null)
        {
            try
            {
                for (String id : playingSounds.keySet())
                {
                    ISound sound = playingSounds.get(id);

                    // Exempt tickable sounds and stun grenade explosions from per-tick muting
                    if (sound == null || sound instanceof ITickableSound || isStunGrenade(sound.getSound().getSoundLocation()))
                        continue;

                    if (!SOUND_VOLUMES.containsKey(id))
                        SOUND_VOLUMES.put(id, sound instanceof SoundMuted ? ((SoundMuted) sound).getVolumeInitial() : soundSystem.getVolume(id));

                    // Reduce volume to full value when duration is above threshold
                    // When below threshold, fade to original sound level as duration approaches 0
                    soundSystem.setVolume(id, getMutedVolume(effect.getDuration(), SOUND_VOLUMES.get(id)));
                }
            }
            catch (ConcurrentModificationException e) {} //SoundManager#playingSounds is accessed from another thread, so it's key set iterator can throw a CME
            isDeafened = true;
        }
        else if (isDeafened)
        {
            // Restore sound levels to initial values
            isDeafened = false;
            for (Entry<String, Float> entry : SOUND_VOLUMES.entrySet())
                soundSystem.setVolume(entry.getKey(), entry.getValue());

            SOUND_VOLUMES.clear();
        }
    }

    @SubscribeEvent
    public static void lowerInitialVolume(PlaySoundEvent event)
    {
        if (soundManager == null)
            soundManager = event.getManager();

        if (!isDeafened || Minecraft.getMinecraft().player == null || event.getSound() instanceof ITickableSound)
            return;

        // Exempt initial explosion from muting
        ResourceLocation loc = event.getSound().getSoundLocation();
        PotionEffect effect = Minecraft.getMinecraft().player.getActivePotionEffect(ModPotions.DEAFENED);
        int duration = effect != null ? effect.getDuration() : 0;
        boolean isStunGrenade = isStunGrenade(loc);
        if (duration == 0 && isStunGrenade)
            return;

        // Reduce volume to full value when duration is above threshold
        // When below threshold, fade to original sound level as duration approaches 0
        event.getSound().createAccessor(Minecraft.getMinecraft().getSoundHandler());
        event.setResultSound(new SoundMuted(event.getSound(), duration, isStunGrenade));
    }

    private static boolean isStunGrenade(ResourceLocation loc)
    {
        return loc.toString().equals(Reference.MOD_ID + ":grenade_stun_explosion");
    }

    private static float getMutedVolume(float duration, float volumeBase)
    {
        float volumeMin = volumeBase * GunConfig.SERVER.stunGrenades.deafen.soundPercentageSynced;
        float percent = Math.min((duration / GunConfig.SERVER.stunGrenades.deafen.soundFadeThresholdSynced), 1);
        return volumeMin + (1 - percent) * (volumeBase - volumeMin);
    }

    public static class SoundMuted implements ISound
    {
        private ISound parent;
        private float volume, volumeInitial;

        public SoundMuted(ISound parent, int duration, boolean isStunGrenade)
        {
            this.parent = parent;
            volumeInitial = MathHelper.clamp(parent.getVolume(), 0, 1);
            volume = SoundEvents.getMutedVolume(duration, volumeInitial);
            if (isStunGrenade)
                volumeInitial = volume;
        }

        @Override
        public float getVolume()
        {
            return volume;
        }

        public float getVolumeInitial()
        {
            return volumeInitial;
        }

        @Override
        public ResourceLocation getSoundLocation()
        {
            return parent.getSoundLocation();
        }

        @Override
        @Nullable
        public SoundEventAccessor createAccessor(SoundHandler handler)
        {
            return parent.createAccessor(handler);
        }

        @Override
        public Sound getSound()
        {
            return parent.getSound();
        }

        @Override
        public SoundCategory getCategory()
        {
            return parent.getCategory();
        }

        @Override
        public boolean canRepeat()
        {
            return parent.canRepeat();
        }

        @Override
        public int getRepeatDelay()
        {
            return parent.getRepeatDelay();
        }

        @Override
        public float getPitch()
        {
            return parent.getPitch();
        }

        @Override
        public float getXPosF()
        {
            return parent.getXPosF();
        }

        @Override
        public float getYPosF()
        {
            return parent.getYPosF();
        }

        @Override
        public float getZPosF()
        {
            return parent.getZPosF();
        }

        @Override
        public ISound.AttenuationType getAttenuationType()
        {
            return parent.getAttenuationType();
        }
    }
}