package com.tac.guns.extra_events;

/**
 * This class will be used for all shooting events that I will utilise.
 * The gun mod provides 3 events for firing guns check out {@link com.mrcrayfish.guns.event.GunFireEvent} for what they are
 */


import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.KeyBinds;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageGunSound;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;


/**
 * Author: ClumsyAlien
 */


@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TacShootingEvent {

    /*
        A bit decent bit of extra code will be locked in external methods such as this, separating some of the standard and advanced
        Functions, especially in order to keep it all clean and allow easy backtracking, however both functions may receive changes
        For now as much of the work I can do will be kept externally such as with fire selection, and burst fire.
        (In short this serves as a temporary test bed to keep development on new functions on course)
    */

    @SubscribeEvent
    public static void preShoot(GunFireEvent.Pre event)
    {
        // Our gun?
        if(!(event.getStack().getItem() instanceof GunItem))
            return;
        HandleFireMode(event);
    }

    private static void HandleFireMode(GunFireEvent.Pre event)
    {
        ItemStack gunItem = event.getStack();
        int[] gunItemFireModes = gunItem.getTag().getIntArray("supportedFireModes");
        Gun gun = ((GunItem) gunItem.getItem()).getModifiedGun(gunItem.getStack()); // Quick patch up, will create static method for handling null supported modes

        if(gunItem.getTag().get("CurrentFireMode") == null) // If user has not checked fire modes yet, default to first mode
        {
            if(ArrayUtils.isEmpty(gunItemFireModes) || gunItemFireModes == null)
            {
                gunItemFireModes = gun.getGeneral().getRateSelector();
                gunItem.getTag().putIntArray("supportedFireModes", gunItemFireModes);
            }
            gunItem.getTag().putInt("CurrentFireMode", gunItemFireModes[0]);
        }

        int currentFireMode = gunItem.getTag().getInt("CurrentFireMode");
        if(currentFireMode == 0)
        {
            if(!Config.COMMON.gameplay.safetyExistence.get())
            {
                gunItem.getTag().remove("CurrentFireMode");
                gunItem.getTag().putInt("CurrentFireMode", gunItemFireModes[currentFireMode+1]);
            }
            else // Safety clicks
            {
                event.getPlayer().sendStatusMessage(new TranslationTextComponent("info." + Reference.MOD_ID + ".gun_safety_lock", new KeybindTextComponent("key.tac.fireSelect").getString().toUpperCase(Locale.ENGLISH)).mergeStyle(TextFormatting.GREEN) ,true);
                event.setCanceled(true);
            }

            ResourceLocation fireModeSound = gun.getSounds().getCock(); // Use cocking sound for now
            if(fireModeSound != null && event.getPlayer().isAlive())
            {
                event.getPlayer().playSound(new SoundEvent(fireModeSound), 1.0F, 1.0F);
            }
        }
    }
}
