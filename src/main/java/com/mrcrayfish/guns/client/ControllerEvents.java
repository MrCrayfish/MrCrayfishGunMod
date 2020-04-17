package com.mrcrayfish.guns.client;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Action;
import com.mrcrayfish.controllable.client.Buttons;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.controllable.event.AvailableActionsEvent;
import com.mrcrayfish.controllable.event.ControllerEvent;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.ReloadHandler;
import com.mrcrayfish.guns.common.CommonEvents;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.ScopeItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageUnload;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Author: MrCrayfish
 */
public class ControllerEvents
{
    private boolean shooting = false;
    private int reloadCounter = -1;

    @SubscribeEvent
    public void onButtonInput(ControllerEvent.ButtonInput event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;
        if(player != null && world != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            switch(event.getButton())
            {
                case Buttons.RIGHT_TRIGGER:
                    if(heldItem.getItem() instanceof GunItem)
                    {
                        event.setCanceled(true);
                        if(event.getState())
                        {
                            GunHandler.fire(player, heldItem);
                        }
                    }
                    break;
                case Buttons.LEFT_TRIGGER:
                    if(heldItem.getItem() instanceof GunItem)
                    {
                        event.setCanceled(true);
                    }
                    break;
                case Buttons.RIGHT_THUMB_STICK:
                    if(heldItem.getItem() instanceof GunItem)
                    {
                        event.setCanceled(true);
                    }
                    break;
                case Buttons.X:
                    if(heldItem.getItem() instanceof GunItem)
                    {
                        event.setCanceled(true);
                        if(event.getState())
                        {
                            reloadCounter = 0;
                        }
                    }
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onControllerTurn(ControllerEvent.Turn event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem && GunMod.proxy.isZooming())
            {
                event.setYawSpeed(10.0F);
                event.setPitchSpeed(7.5F);

                ItemStack scope = Gun.getScope(heldItem);
                if (scope != null)
                {
                    ScopeItem.Type scopeType = ScopeItem.Type.getFromStack(scope);
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
        Minecraft mc = Minecraft.getInstance();
        if(mc.currentScreen != null)
            return;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                event.getActions().put(Buttons.LEFT_TRIGGER, new Action("Aim", Action.Side.RIGHT));
                event.getActions().put(Buttons.RIGHT_TRIGGER, new Action("Shoot", Action.Side.RIGHT));

                GunItem gunItem = (GunItem) heldItem.getItem();
                CompoundNBT tag = heldItem.getTagCompound();
                if(tag != null && tag.getInteger("AmmoCount") < gunItem.getGun().general.maxAmmo)
                {
                    event.getActions().put(Buttons.X, new Action("Reload", Action.Side.LEFT));
                }

                ItemStack scope = Gun.getScope(heldItem);
                if (scope != null && GunMod.proxy.isZooming())
                {
                    ScopeItem.Type scopeType = ScopeItem.Type.getFromStack(scope);
                    if(scopeType == ScopeItem.Type.LONG)
                    {
                        event.getActions().put(Buttons.RIGHT_THUMB_STICK, new Action("Hold Breath", Action.Side.RIGHT));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event)
    {
        Controller controller = Controllable.getController();
        if(controller == null)
            return;

        if(event.phase == TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null)
            return;

        if(controller.getState().rightTrigger > 0.05)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
                if(gun.general.auto)
                {
                    GunHandler.fire(player, heldItem);
                }
            }
        }

        if(mc.currentScreen == null && reloadCounter != -1)
        {
            if(controller.getState().x)
            {
                reloadCounter++;
            }
        }

        if(reloadCounter > 40)
        {
            ReloadHandler.setReloading(false);
            PacketHandler.INSTANCE.sendToServer(new MessageUnload());
            reloadCounter = -1;
        }
        else if(reloadCounter > 0 && !controller.getState().x)
        {
            if(!Minecraft.getInstance().player.getDataManager().get(CommonEvents.RELOADING))
            {
                ReloadHandler.setReloading(true);
                ReloadHandler.reloadingSlot = Minecraft.getInstance().player.inventory.currentItem;
            }
            else
            {
                ReloadHandler.setReloading(false);
            }
            reloadCounter = -1;
        }
    }
}
