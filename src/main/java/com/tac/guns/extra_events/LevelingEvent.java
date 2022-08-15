package com.tac.guns.extra_events;

/**
 * This class will be used for all shooting events that I will utilise.
 * The gun mod provides 3 events for firing guns check out {@link com.mrcrayfish.guns.event.GunFireEvent} for what they are
 */


import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.event.LevelUpEvent;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.MBPGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Locale;


/**
 * Author: ClumsyAlien
 */


@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LevelingEvent {

    /*
        A bit decent bit of extra code will be locked in external methods such as this, separating some of the standard and advanced
        Functions, especially in order to keep it all clean and allow easy backtracking, however both functions may receive changes
        For now as much of the work I can do will be kept externally such as with fire selection, and burst fire.
        (In short this serves as a temporary test bed to keep development on new functions on course)
    */

    @SubscribeEvent
    public void onPartialLevel(LevelUpEvent.Post event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        event.getPlayer().sendStatusMessage(new TranslationTextComponent("info." + Reference.MOD_ID + ".gun_waterlock"), true);
    }

}
