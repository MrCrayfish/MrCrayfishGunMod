package com.mrcrayfish.guns.client.audio;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModEffects;
import com.mrcrayfish.guns.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

import net.minecraft.client.audio.ISound.AttenuationType;

public class StunRingingSound extends TickableSound
{
    public StunRingingSound()
    {
        super(ModSounds.ENTITY_STUN_GRENADE_RING.get(), SoundCategory.MASTER);
        this.looping = true;
        this.attenuation = AttenuationType.NONE;
        this.tick();
    }

    @Override
    public void tick()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null && player.isAlive())
        {
            EffectInstance effect = player.getEffect(ModEffects.DEAFENED.get());
            if(effect != null)
            {
                this.x = (float) player.getX();
                this.y = (float) player.getY();
                this.z = (float) player.getZ();
                float percent = Math.min((effect.getDuration() / (float) Config.SERVER.soundFadeThreshold.get()), 1);
                this.volume = (float) (percent * Config.SERVER.ringVolume.get());
                return;
            }
        }

        //Stops playing the sound
        this.stop();
    }
}