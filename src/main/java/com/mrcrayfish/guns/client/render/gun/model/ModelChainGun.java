package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
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
    private boolean init = false;
    private IBakedModel base;
    private IBakedModel barrel;

    private int lastRotation;
    private int rotation;

    @Override
    public void init()
    {
        if(init) return;
        base = RenderUtil.getModel(ModGuns.PARTS, 0);
        barrel = RenderUtil.getModel(ModGuns.PARTS, 1);
        init = true;
    }

    @Override
    public void tick(EntityLivingBase entity)
    {
        lastRotation = rotation;
        boolean shooting = Mouse.isButtonDown(GunConfig.CLIENT.controls.oldControls ? 1 : 0);

        if(ClientProxy.controllableLoaded)
        {
            Controller controller = Controllable.getController();
            if(controller != null)
            {
                shooting |= controller.getState().rightTrigger >= 0.5;
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
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, EntityLivingBase entity)
    {
        RenderUtil.renderModel(base, transformType, stack);
        RenderUtil.renderModel(barrel, transformType, () -> RenderUtil.rotateZ(0.5F, 0.125F, lastRotation + (rotation - lastRotation) * partialTicks), stack, ItemStack.EMPTY);
    }
}
