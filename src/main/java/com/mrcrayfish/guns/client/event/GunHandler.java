package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.AimTracker;
import com.mrcrayfish.guns.common.CommonEvents;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAim;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class GunHandler
{
    private static final Map<UUID, AimTracker> AIMING_MAP = new HashMap<>();
    public static boolean aiming = false;

    @SubscribeEvent
    public void onKeyPressed(MouseEvent event)
    {
        if(!Minecraft.getMinecraft().inGameHasFocus)
            return;

        if(!event.isButtonstate())
            return;

        Minecraft mc = Minecraft.getMinecraft();
        if(event.getButton() == 1 && ClientProxy.isLookingAtInteract())
        {
            if(mc.player.getHeldItemMainhand().getItem() instanceof GunItem && !ClientProxy.isLookingAtInteractBlock())
            {
                event.setCanceled(true);
            }
            return;
        }

        PlayerEntity player = mc.player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                int button = event.getButton();
                if(!Config.CLIENT.controls.oldControls)
                {
                    if(button == 0 || button == 1)
                    {
                        event.setCanceled(true);
                    }

                    if(event.isButtonstate() && button == 0)
                    {
                        fire(player, heldItem);
                    }
                }
                else if(event.isButtonstate() && button == 1)
                {
                    event.setCanceled(true);
                    fire(player, heldItem);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPostClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        if(!Minecraft.getMinecraft().inGameHasFocus)
            return;

        PlayerEntity player = Minecraft.getMinecraft().player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                 Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
                 if(gun.general.auto)
                 {
                     if(Mouse.isButtonDown(Config.CLIENT.controls.oldControls ? 1 : 0))
                     {
                         fire(player, heldItem);
                     }
                 }
            }
        }
    }

    public static void fire(PlayerEntity player, ItemStack heldItem)
    {
        if(!(heldItem.getItem() instanceof GunItem))
            return;

        if(!GunItem.hasAmmo(heldItem) && !player.capabilities.isCreativeMode)
            return;
        
        if(player.isSpectator())
            return;

        CooldownTracker tracker = player.getCooldownTracker();
        if(!tracker.hasCooldown(heldItem.getItem()))
        {
            GunItem gunItem = (GunItem) heldItem.getItem();
            Gun modifiedGun = gunItem.getModifiedGun(heldItem);
            tracker.setCooldown(heldItem.getItem(), modifiedGun.general.rate);
            RenderEvents.getCooldownTracker(player.getUniqueID()).setCooldown(heldItem.getItem(), modifiedGun.general.rate);
            PacketHandler.INSTANCE.sendToServer(new MessageShoot());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        if(Minecraft.getMinecraft().player == null)
            return;

        if(GunMod.proxy.isZooming())
        {
            if(!aiming)
            {
                Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.AIMING, true);
                PacketHandler.INSTANCE.sendToServer(new MessageAim(true));
                aiming = true;
            }
        }
        else if(aiming)
        {
            Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.AIMING, false);
            PacketHandler.INSTANCE.sendToServer(new MessageAim(false));
            aiming = false;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = event.player;
        AimTracker tracker = getAimTracker(player);
        if(tracker != null)
        {
            tracker.handleAiming(player);
            if(!tracker.isAiming())
            {
                AIMING_MAP.remove(player.getUniqueID());
            }
        }
    }

    @Nullable
    private static AimTracker getAimTracker(PlayerEntity player)
    {
        if(player.getDataManager().get(CommonEvents.AIMING) && !AIMING_MAP.containsKey(player.getUniqueID()))
        {
            AIMING_MAP.put(player.getUniqueID(), new AimTracker());
        }
        return AIMING_MAP.get(player.getUniqueID());
    }

    public static float getAimProgress(PlayerEntity player, float partialTicks)
    {
        AimTracker tracker = getAimTracker(player);
        if(tracker != null)
        {
            return tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }
}
