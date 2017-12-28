package com.mrcrayfish.guns.client.render.model;

import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.item.ItemGun;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class OverrideModelPlayer extends ModelPlayer
{
    public OverrideModelPlayer(float modelSize, boolean smallArmsIn)
    {
        super(modelSize, smallArmsIn);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if(true) return;


        EntityPlayer player = (EntityPlayer) entityIn;
        ItemStack stack = player.getHeldItemMainhand();

        if(!stack.isEmpty() && stack.getItem() instanceof ItemGun)
        {
            if(player.getEntityData().getCompoundTag("cgm").getBoolean("aiming"))
            {

            }
            else
            {

            }
        }

        this.bipedLeftArm.rotationPointX = 2.0F;
        this.bipedLeftArm.rotationPointZ = -2.0F;

        this.bipedRightArm.rotateAngleX = (float) Math.toRadians(20) + (float) Math.toRadians(entityIn.rotationPitch);
        this.bipedRightArm.rotateAngleY = 0F;
        this.bipedRightArm.rotateAngleZ = 0F;

        float percent = (entityIn.rotationPitch + 90F) / 180F - 0.5F;
        this.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-30) + (float) Math.toRadians(entityIn.rotationPitch);
        this.bipedLeftArm.rotateAngleY = (float) Math.toRadians(35) + (float) Math.toRadians(50F * -percent);;
        this.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(25) + (float) Math.toRadians(20F * percent);
    }
}
