package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Buttons;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

/**
 * Author: MrCrayfish
 */
public class ModelChainGun implements IOverrideModel
{
    private int lastRotation;
    private int rotation;

    @Override
    public void init() {}

    @Override
    public void tick(EntityLivingBase entity)
    {
        lastRotation = rotation;
        boolean shooting = Minecraft.getMinecraft().inGameHasFocus && Mouse.isButtonDown(GunConfig.CLIENT.controls.oldControls ? 1 : 0);

        if(ClientProxy.controllableLoaded)
        {
            Controller controller = Controllable.getController();
            if(controller != null)
            {
                shooting |= controller.isButtonPressed(Buttons.RIGHT_TRIGGER);
            }
        }

        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!ItemGun.hasAmmo(heldItem) && !player.capabilities.isCreativeMode)
        {
            shooting = false;
        }

        if(shooting)
        {
            rotation += 20;
        }
        else
        {
            rotation += 1;
        }
    }

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, EntityLivingBase entity)
    {
        RenderUtil.renderModel(RenderUtil.getModel(ModGuns.PARTS, 0), stack);
        RenderUtil.renderModel(RenderUtil.getModel(ModGuns.PARTS, 1), ItemCameraTransforms.TransformType.NONE, () -> RenderUtil.rotateZ(0.5F, 0.125F, lastRotation + (rotation - lastRotation) * partialTicks), stack, ItemStack.EMPTY);
    }
}
