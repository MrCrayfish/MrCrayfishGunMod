package com.mrcrayfish.guns.client.audio;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModEffects;
import com.mrcrayfish.guns.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

public class StunRingingSound extends TickableSound
{
    public StunRingingSound()
    {
        super(ModSounds.ENTITY_STUN_GRENADE_RING.get(), SoundCategory.MASTER);
        this.repeat = true;
        this.attenuationType = AttenuationType.NONE;
        this.tick();
    }

    @Override
    public void tick()
    {
        this.donePlaying = true;
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null && player.isAlive())
        {
            EffectInstance effect = player.getActivePotionEffect(ModEffects.DEAFENED.get());
            if(effect != null)
            {
                this.x = (float) player.getPosX();
                this.y = (float) player.getPosY();
                this.z = (float) player.getPosZ();
                float percent = Math.min((effect.getDuration() / (float) Config.SERVER.soundFadeThreshold.get()), 1);
                this.volume = (float) (percent * Config.SERVER.ringVolume.get());
                this.donePlaying = false;
            }
        }
    }
}