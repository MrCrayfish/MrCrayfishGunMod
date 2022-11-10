package com.tac.guns.extra_events;

import java.util.Locale;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.entity.DamageSourceProjectile;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.event.LevelUpEvent;
import com.tac.guns.init.ModSounds;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.TransitionalTypes.M1GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

/**
 * This class will be used for all shooting events that I will utilise.
 * The gun mod provides 3 events for firing guns check out {@link com.mrcrayfish.guns.event.GunFireEvent} for what they are
 */


import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.item.GunItem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


/**
 * Author: ClumsyAlien
 */


@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TacEventListeners {

    /*
        A bit decent bit of extra code will be locked in external methods such as this, separating some of the standard and advanced
        Functions, especially in order to keep it all clean and allow easy backtracking, however both functions may receive changes
        For now as much of the work I can do will be kept externally such as with fire selection, and burst fire.
        (In short this serves as a temporary test bed to keep development on new functions on course)
    */

    @SubscribeEvent
    public void onPartialLevel(LevelUpEvent.Post event)
    {
        PlayerEntity player = event.getPlayer();
        event.getPlayer().getEntityWorld().playSound(player, player.getPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundCategory.PLAYERS,4.0F, 1.0F);
    }

    /* BTW this was by bomb787 as a Timeless Contributor */
    @SubscribeEvent
    public static void postShoot(GunFireEvent.Post event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!(heldItem.getItem() instanceof M1GunItem))
            return;
        CompoundNBT tag = heldItem.getTag();
        if(tag != null)
        {
            if(tag.getInt("AmmoCount") == 1)
                event.getPlayer().getEntityWorld().playSound(player, player.getPosition(), ModSounds.M1_PING.get()/*.GARAND_PING.get()*/, SoundCategory.MASTER, 3.0F, 1.0F);
        }
    }




    @SubscribeEvent
    public static void handleArmor(LivingHurtEvent event)
    {
        if(event.getSource().damageType == "bullet")
        {
            /*if(Entity.hasTacArmor()) {
                float totalArmorDurrability = armor.getDurrability();
                if (totalArmorDurrability > 0) {
                    //apply damage to armor
                }
            }*/
        }
    }




}
