package com.mrcrayfish.guns.client.audio;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModPotions;
import com.mrcrayfish.guns.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;

public class SoundRinging extends MovingSound
{
    public SoundRinging()
    {
        super(ModSounds.getSound("cgm:grenade_stun_ring"), SoundCategory.MASTER);
        repeat = true;
        attenuationType = AttenuationType.NONE;
        update();
    }

    @Override
    public void update()
    {
        donePlaying = true;
        PlayerEntity player = Minecraft.getMinecraft().player;
        if (player != null && !player.isDead)
        {
            PotionEffect effect = player.getActivePotionEffect(ModPotions.DEAFENED);
            if (effect != null)
            {
                xPosF = (float) player.posX;
                yPosF = (float) player.posY;
                zPosF = (float) player.posZ;
                float percent = Math.min((effect.getDuration() / (float) Config.SERVER.stunGrenades.deafen.soundFadeThresholdSynced), 1);
                volume = percent * Config.SERVER.stunGrenades.deafen.ringVolumeSynced;
                donePlaying = false;
            }
        }
    }
}