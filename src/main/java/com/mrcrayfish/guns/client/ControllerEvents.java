package com.mrcrayfish.guns.client;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Action;
import com.mrcrayfish.controllable.client.Buttons;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.controllable.event.AvailableActionsEvent;
import com.mrcrayfish.controllable.event.ControllerEvent;
import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.ReloadHandler;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageUnload;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = Minecraft.getMinecraft().world;
        if(player != null && world != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            switch(event.getButton())
            {
                case Buttons.RIGHT_TRIGGER:
                    if(heldItem.getItem() instanceof ItemGun)
                    {
                        event.setCanceled(true);
                        GunHandler.fire(player, heldItem);
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
                case Buttons.X:
                    if(heldItem.getItem() instanceof ItemGun)
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

                ItemGun itemGun = (ItemGun) heldItem.getItem();
                NBTTagCompound tag = heldItem.getTagCompound();
                if(tag != null && tag.getInteger("AmmoCount") < itemGun.getGun().general.maxAmmo)
                {
                    event.getActions().put(Buttons.X, new Action("Reload", Action.Side.LEFT));
                }

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

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event)
    {
        Controller controller = Controllable.getController();
        if(controller == null)
            return;

        if(event.phase == TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if(player == null)
            return;

        if(controller.getState().rightTrigger > 0.05)
        {
            GunHandler.fire(player, player.getHeldItemMainhand());
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
            if(!Minecraft.getMinecraft().player.getDataManager().get(CommonEvents.RELOADING))
            {
                ReloadHandler.setReloading(true);
                ReloadHandler.reloadingSlot = Minecraft.getMinecraft().player.inventory.currentItem;
            }
            else
            {
                ReloadHandler.setReloading(false);
            }
            reloadCounter = -1;
        }
    }
}
