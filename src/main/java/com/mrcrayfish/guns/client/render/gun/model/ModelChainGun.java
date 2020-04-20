package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

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
    public void tick(LivingEntity entity)
    {
        this.lastRotation = this.rotation;

        Minecraft mc = Minecraft.getInstance();
        boolean shooting = mc.isGameFocused() && GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

        if(ClientProxy.controllableLoaded)
        {
            Controller controller = Controllable.getController();
            if(controller != null)
            {
                shooting |= controller.getRTriggerValue() >= 0.5;
            }
        }

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(!Gun.hasAmmo(heldItem) && !player.isCreative())
            {
                shooting = false;
            }
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
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity)
    {
        //TODO register back gun parts
        //RenderUtil.renderModel(RenderUtil.getModel(ModItems.PARTS, 0), stack);
        //RenderUtil.renderModel(RenderUtil.getModel(ModItems.PARTS, 1), ItemCameraTransforms.TransformType.NONE, () -> RenderUtil.rotateZ(0.5F, 0.125F, lastRotation + (rotation - lastRotation) * partialTicks), stack, ItemStack.EMPTY);
    }
}
