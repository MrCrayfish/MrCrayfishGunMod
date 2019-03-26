package com.mrcrayfish.guns.client;

import com.mrcrayfish.controllable.client.Action;
import com.mrcrayfish.controllable.client.Buttons;
import com.mrcrayfish.controllable.event.AvailableActionsEvent;
import com.mrcrayfish.controllable.event.ControllerEvent;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author: MrCrayfish
 */
public class ControllerEvents
{
    private boolean shooting = false;

    @SubscribeEvent
    public void onButtonInput(ControllerEvent.ButtonInput event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = Minecraft.getMinecraft().world;
        if(player != null && world != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            switch(event.getButton())
            {
                case Buttons.RIGHT_TRIGGER:
                    if(heldItem.getItem() instanceof ItemGun || shooting)
                    {
                        event.setButton(Buttons.LEFT_TRIGGER);
                        shooting = event.getState();
                    }
                    break;
                case Buttons.LEFT_TRIGGER:
                    if(heldItem.getItem() instanceof ItemGun)
                    {
                        event.setCanceled(true);
                    }
                    break;
                case Buttons.RIGHT_THUMB_STICK:
                    if(heldItem.getItem() instanceof ItemGun)
                    {
                        event.setCanceled(true);
                    }
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onControllerTurn(ControllerEvent.Turn event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof ItemGun && MrCrayfishGunMod.proxy.isZooming())
            {
                event.setYawSpeed(10.0F);
                event.setPitchSpeed(7.5F);

                ItemStack scope = Gun.getScope(heldItem);
                if (scope != null)
                {
                    ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);
                    if(scopeType != null)
                    {
                        switch(scopeType)
                        {
                            case LONG:
                                if(event.getController().getState().rightStickClick)
                                {
                                    event.setYawSpeed(1.5F);
                                    event.setPitchSpeed(1.0F);
                                }
                                else
                                {
                                    event.setYawSpeed(3.5F);
                                    event.setPitchSpeed(3.0F);
                                }
                                break;
                            case MEDIUM:
                                event.setYawSpeed(6.66F);
                                event.setPitchSpeed(5.0F);
                                break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void updateAvailableActions(AvailableActionsEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.currentScreen != null)
            return;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof ItemGun)
            {
                event.getActions().put(Buttons.LEFT_TRIGGER, new Action("Aim", Action.Side.RIGHT));
                event.getActions().put(Buttons.RIGHT_TRIGGER, new Action("Shoot", Action.Side.RIGHT));

                ItemStack scope = Gun.getScope(heldItem);
                if (scope != null && MrCrayfishGunMod.proxy.isZooming())
                {
                    ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);
                    if(scopeType == ItemScope.Type.LONG)
                    {
                        event.getActions().put(Buttons.RIGHT_THUMB_STICK, new Action("Hold Breath", Action.Side.RIGHT));
                    }
                }
            }
        }
    }
}
