package com.mrcrayfish.guns.client.audio;

import com.mrcrayfish.guns.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.LocatableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import net.minecraft.client.audio.ISound.AttenuationType;

/**
 * Author: MrCrayfish
 */
public class GunShotSound extends LocatableSound
{
    public GunShotSound(ResourceLocation soundIn, SoundCategory categoryIn, float x, float y, float z, float volume, float pitch, boolean reload)
    {
        super(soundIn, categoryIn);
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.attenuation = AttenuationType.NONE;

        ClientPlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            float distance = reload ? Config.SERVER.reloadMaxDistance.get().floatValue() : Config.SERVER.gunShotMaxDistance.get().floatValue();
            this.volume = volume * (1.0F - Math.min(1.0F, (float) Math.sqrt(player.distanceToSqr(x, y, z)) / distance));
            this.volume *= this.volume; //Ease the volume instead of linear
        }
    }
}
