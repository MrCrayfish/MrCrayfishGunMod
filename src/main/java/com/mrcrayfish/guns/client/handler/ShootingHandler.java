package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.compat.PlayerReviveHelper;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.network.message.MessageShooting;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import org.lwjgl.glfw.GLFW;

/**
 * Author: MrCrayfish
 */
public class ShootingHandler
{
    private static ShootingHandler instance;

    public static ShootingHandler get()
    {
        if(instance == null)
        {
            instance = new ShootingHandler();
        }
        return instance;
    }

    private boolean shooting;

    private ShootingHandler() {}

    private boolean isInGame()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.overlay != null)
            return false;
        if(mc.screen != null)
            return false;
        if(!mc.mouseHandler.isMouseGrabbed())
            return false;
        return mc.isWindowActive();
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.RawMouseEvent event)
    {
        if(!this.isInGame())
            return;

        if(event.getAction() != GLFW.GLFW_PRESS)
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null)
            return;

        if(PlayerReviveHelper.isBleeding(player))
            return;

        if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && AimingHandler.get().isLookingAtInteractableBlock())
        {
            if(player.getMainHandItem().getItem() instanceof GunItem && !AimingHandler.get().isLookingAtInteractableBlock())
            {
                event.setCanceled(true);
            }
            return;
        }

        ItemStack heldItem = player.getMainHandItem();
        if(heldItem.getItem() instanceof GunItem && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMouseClick(InputEvent.ClickInputEvent event)
    {
        if(event.isCanceled())
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null)
            return;

        if(PlayerReviveHelper.isBleeding(player))
            return;

        if(event.getKeyBinding() == mc.options.keyAttack)
        {
            ItemStack heldItem = player.getMainHandItem();
            if(heldItem.getItem() instanceof GunItem)
            {
                event.setSwingHand(false);
                event.setCanceled(true);
                this.fire(player, heldItem);
            }
        }
    }

    /* Prevents the right click animation playing when interacting with a block while holding a weapon */
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if(event.getSide() == LogicalSide.CLIENT)
        {
            ItemStack heldItem = event.getItemStack();
            if(heldItem.getItem() instanceof GunItem)
            {
                event.setCancellationResult(ActionResultType.CONSUME);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onHandleShooting(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        if(!this.isInGame())
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player != null)
        {
            ItemStack heldItem = player.getMainHandItem();
            if(heldItem.getItem() instanceof GunItem && (Gun.hasAmmo(heldItem) || player.isCreative()) && !PlayerReviveHelper.isBleeding(player))
            {
                boolean shooting = GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
                if(GunMod.controllableLoaded)
                {
                    shooting |= ControllerHandler.isShooting();
                }
                if(shooting)
                {
                    if(!this.shooting)
                    {
                        this.shooting = true;
                        PacketHandler.getPlayChannel().sendToServer(new MessageShooting(true));
                    }
                }
                else if(this.shooting)
                {
                    this.shooting = false;
                    PacketHandler.getPlayChannel().sendToServer(new MessageShooting(false));
                }
            }
            else if(this.shooting)
            {
                this.shooting = false;
                PacketHandler.getPlayChannel().sendToServer(new MessageShooting(false));
            }
        }
        else
        {
            this.shooting = false;
        }
    }

    @SubscribeEvent
    public void onPostClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        if(!isInGame())
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player != null)
        {
            if(PlayerReviveHelper.isBleeding(player))
                return;

            ItemStack heldItem = player.getMainHandItem();
            if(heldItem.getItem() instanceof GunItem)
            {
                if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS)
                {
                    Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
                    if(gun.getGeneral().isAuto())
                    {
                        this.fire(player, heldItem);
                    }
                }
            }
        }
    }

    public void fire(PlayerEntity player, ItemStack heldItem)
    {
        if(!(heldItem.getItem() instanceof GunItem))
            return;

        if(!Gun.hasAmmo(heldItem) && !player.isCreative())
            return;
        
        if(player.isSpectator())
            return;

        CooldownTracker tracker = player.getCooldowns();
        if(!tracker.isOnCooldown(heldItem.getItem()))
        {
            GunItem gunItem = (GunItem) heldItem.getItem();
            Gun modifiedGun = gunItem.getModifiedGun(heldItem);

            if(MinecraftForge.EVENT_BUS.post(new GunFireEvent.Pre(player, heldItem)))
                return;

            int rate = GunEnchantmentHelper.getRate(heldItem, modifiedGun);
            rate = GunModifierHelper.getModifiedRate(heldItem, rate);
            tracker.addCooldown(heldItem.getItem(), rate);
            PacketHandler.getPlayChannel().sendToServer(new MessageShoot(player));

            MinecraftForge.EVENT_BUS.post(new GunFireEvent.Post(player, heldItem));
        }
    }
}
