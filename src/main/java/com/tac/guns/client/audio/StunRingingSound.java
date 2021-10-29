package com.tac.guns.client.audio;

import com.tac.guns.Config;
import com.tac.guns.init.ModEffects;
import com.tac.guns.init.ModSounds;
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
                return;
            }
        }

        //Stops playing the sound
        this.finishPlaying();
    }
}