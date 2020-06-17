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
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.ScopeItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAttachments;
import com.mrcrayfish.guns.network.message.MessageUnload;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.Scope;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: MrCrayfish
 */
public class ControllerEvents
{
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
                case Buttons.DPAD_LEFT:
                    if(heldItem.getItem() instanceof GunItem && Minecraft.getInstance().currentScreen == null)
                    {
                        event.setCanceled(true);
                        if(event.getState())
                        {
                            PacketHandler.getPlayChannel().sendToServer(new MessageAttachments());
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
            if(heldItem.getItem() instanceof GunItem && ClientHandler.isAiming())
            {
                event.setYawSpeed(10.0F * (float) GunMod.getOptions().getAdsSensitivity());
                event.setPitchSpeed(7.5F * (float) GunMod.getOptions().getAdsSensitivity());

                Scope scope = Gun.getScope(heldItem);
                if(scope != null && scope.isStable() && event.getController().getButtonsStates().getState(Buttons.RIGHT_THUMB_STICK))
                {
                    event.setYawSpeed(event.getYawSpeed() / 2.0F);
                    event.setPitchSpeed(event.getPitchSpeed() / 2.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public void updateAvailableActions(AvailableActionsEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.currentScreen != null) return;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                event.getActions().put(Buttons.LEFT_TRIGGER, new Action("Aim", Action.Side.RIGHT));
                event.getActions().put(Buttons.RIGHT_TRIGGER, new Action("Shoot", Action.Side.RIGHT));

                GunItem gunItem = (GunItem) heldItem.getItem();
                CompoundNBT tag = heldItem.getTag();
                if(tag != null && tag.getInt("AmmoCount") < gunItem.getModifiedGun(heldItem).general.maxAmmo)
                {
                    event.getActions().put(Buttons.X, new Action("Reload", Action.Side.LEFT));
                }

                ItemStack scopeStack = Gun.getScopeStack(heldItem);
                if(scopeStack.getItem() instanceof ScopeItem && ClientHandler.isAiming())
                {
                    ScopeItem scopeItem = (ScopeItem) scopeStack.getItem();
                    Scope scope = scopeItem.getScope();
                    if(scope.isStable())
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
        if(controller == null) return;

        if(event.phase == TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null) return;

        if(controller.getRTriggerValue() > 0.05)
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

        if(mc.currentScreen == null && this.reloadCounter != -1)
        {
            if(controller.getButtonsStates().getState(Buttons.X))
            {
                this.reloadCounter++;
            }
        }

        if(this.reloadCounter > 40)
        {
            ReloadHandler.setReloading(false);
            PacketHandler.getPlayChannel().sendToServer(new MessageUnload());
            this.reloadCounter = -1;
        }
        else if(this.reloadCounter > 0 && !controller.getButtonsStates().getState(Buttons.X))
        {
            ReloadHandler.setReloading(!SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING));
            reloadCounter = -1;
        }
    }
}
