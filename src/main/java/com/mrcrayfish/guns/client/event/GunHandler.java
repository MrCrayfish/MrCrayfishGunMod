package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.AimTracker;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

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
    public static void onKeyPressed(InputEvent.RawMouseEvent event)
    {
        if(!Minecraft.getInstance().isGameFocused())
            return;

        if(event.getAction() != GLFW.GLFW_PRESS)
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null)
            return;

        if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && ClientHandler.isLookingAtInteractableBlock())
        {
            if(player.getHeldItemMainhand().getItem() instanceof GunItem && !ClientHandler.isLookingAtInteractableBlock())
            {
                event.setCanceled(true);
            }
            return;
        }

        ItemStack heldItem = player.getHeldItemMainhand();
        if(heldItem.getItem() instanceof GunItem)
        {
            int button = event.getButton();
            if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
            {
                event.setCanceled(true);
            }
            if(event.getAction() == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
            {
                fire(player, heldItem);
            }
        }
    }

    @SubscribeEvent
    public static void onPostClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(!mc.isGameFocused())
            return;

        PlayerEntity player = mc.player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                 Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
                 if(gun.general.auto)
                 {
                     if(GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS)
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

        if(!GunItem.hasAmmo(heldItem) && !player.isCreative())
            return;
        
        if(player.isSpectator())
            return;

        CooldownTracker tracker = player.getCooldownTracker();
        if(!tracker.hasCooldown(heldItem.getItem()))
        {
            GunItem gunItem = (GunItem) heldItem.getItem();
            Gun modifiedGun = gunItem.getModifiedGun(heldItem);
            tracker.setCooldown(heldItem.getItem(), modifiedGun.general.rate);
            PacketHandler.getPlayChannel().sendToServer(new MessageShoot());
        }
    }

    public static boolean isAiming()
    {
        Minecraft mc = Minecraft.getInstance();
        if(!mc.isGameFocused())
            return false;

        if(mc.player == null)
            return false;

        if(mc.player.isSpectator())
            return false;

        if(!(mc.player.inventory.getCurrentItem().getItem() instanceof GunItem))
            return false;

        boolean zooming = GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if(GunMod.controllableLoaded)
        {
            Controller controller = Controllable.getController();
            if(controller != null)
            {
                zooming |= controller.getLTriggerValue() >= 0.5;
            }
        }

        return zooming;
    }
}
