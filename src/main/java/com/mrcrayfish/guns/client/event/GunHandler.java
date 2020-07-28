package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.network.message.MessageShooting;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class GunHandler
{
    private static float recoil;
    private static float progressRecoil;
    private static boolean shooting;

    private static boolean isInGame()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.loadingGui != null)
            return false;
        if(mc.currentScreen != null)
            return false;
        if(!mc.mouseHelper.isMouseGrabbed())
            return false;
        return mc.isGameFocused();
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.RawMouseEvent event)
    {
        if(!isInGame())
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
    public static void onHandleShooting(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        if(!isInGame())
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem && (Gun.hasAmmo(heldItem) || player.isCreative()))
            {
                boolean shooting = GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
                if(GunMod.controllableLoaded)
                {
                    Controller controller = Controllable.getController();
                    if(controller != null)
                    {
                        shooting |= controller.getRTriggerValue() > 0.5;
                    }
                }
                if(shooting)
                {
                    if(!GunHandler.shooting)
                    {
                        GunHandler.shooting = true;
                        PacketHandler.getPlayChannel().sendToServer(new MessageShooting(true));
                    }
                }
                else if(GunHandler.shooting)
                {
                    GunHandler.shooting = false;
                    PacketHandler.getPlayChannel().sendToServer(new MessageShooting(false));
                }
            }
            else if(GunHandler.shooting)
            {
                GunHandler.shooting = false;
                PacketHandler.getPlayChannel().sendToServer(new MessageShooting(false));
            }
        }
        else
        {
            GunHandler.shooting = false;
        }
    }

    @SubscribeEvent
    public static void onPostClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        if(!isInGame())
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                if(GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS)
                {
                    Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
                    if(gun.general.auto)
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

        if(!Gun.hasAmmo(heldItem) && !player.isCreative())
            return;
        
        if(player.isSpectator())
            return;

        CooldownTracker tracker = player.getCooldownTracker();
        if(!tracker.hasCooldown(heldItem.getItem()))
        {
            GunItem gunItem = (GunItem) heldItem.getItem();
            Gun modifiedGun = gunItem.getModifiedGun(heldItem);

            int rate = GunEnchantmentHelper.getRate(heldItem, modifiedGun);
            rate = GunModifierHelper.getModifiedRate(heldItem, rate);
            tracker.setCooldown(heldItem.getItem(), rate);
            PacketHandler.getPlayChannel().sendToServer(new MessageShoot(player));
            if(modifiedGun.display.flash != null)
            {
                ClientHandler.getGunRenderer().showMuzzleFlash();
            }
            if(Config.SERVER.enableCameraRecoil.get())
            {
                float recoilModifier = 1.0F - GunModifierHelper.getRecoilModifier(heldItem);
                recoil = modifiedGun.general.recoilAngle * recoilModifier;
                progressRecoil = 0F;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(!Config.SERVER.enableCameraRecoil.get())
        {
            return;
        }
        if(event.phase == TickEvent.Phase.END && recoil > 0)
        {
            Minecraft mc = Minecraft.getInstance();
            if(mc.player != null)
            {
                float recoilAmount = recoil * mc.getTickLength() * 0.1F;
                float startProgress = progressRecoil / recoil;
                progressRecoil += recoilAmount;
                float endProgress = progressRecoil / recoil;

                if(startProgress < 0.2F)
                {
                    mc.player.rotationPitch -= ((endProgress - startProgress) / 0.2F) * recoil;
                }
                else
                {
                    mc.player.rotationPitch += ((endProgress - startProgress) / 0.8F) * recoil;
                }

                if(progressRecoil >= recoil)
                {
                    recoil = 0;
                    progressRecoil = 0;
                }
            }
        }
    }
}
