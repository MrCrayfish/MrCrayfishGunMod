package com.tac.guns.extra_events;


import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Locale;

/**
 * Author: ClumsyAlien
 */


@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TacShootingEvent
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void preShoot(GunFireEvent.Pre event)
    {
        if(!(event.getStack().getItem() instanceof TimelessGunItem))
            return;
        HandleFireMode(event);
    }
    private static void HandleFireMode(GunFireEvent.Pre event)
    {
        ItemStack gunItem = event.getStack();
        int[] gunItemFireModes = gunItem.getTag().getIntArray("supportedFireModes");
        Gun gun = ((GunItem) gunItem.getItem()).getModifiedGun(gunItem.getStack()); // Quick patch up, will create static method for handling null supported modes
        /*float dist =
        (Math.abs(event.getPlayer().moveForward)/4+
                Math.abs(event.getPlayer().moveStrafing)/1.5f)*
                (event.getPlayer().moveVertical != 0 ? 3:1);
        if(dist != 0)
            SyncedPlayerData.instance().set(event.getPlayer(), ModSyncedDataKeys.MOVING, dist);
        else
            SyncedPlayerData.instance().set(event.getPlayer(), ModSyncedDataKeys.MOVING, 0f);*/
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
