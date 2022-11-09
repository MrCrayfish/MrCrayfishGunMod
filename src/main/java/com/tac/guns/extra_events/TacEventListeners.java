package com.tac.guns.extra_events;

import java.util.Locale;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.entity.DamageSourceProjectile;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.event.LevelUpEvent;
import com.tac.guns.init.ModSounds;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.TransitionalTypes.M1GunItem;
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
    public static void preShoot(GunFireEvent.Pre event)
    {
        if(!(event.getStack().getItem() instanceof GunItem))
            return;
        HandleFireMode(event);
    }
    private static void HandleFireMode(GunFireEvent.Pre event)
    {
        ItemStack gunItem = event.getStack();
        int[] gunItemFireModes = gunItem.getTag().getIntArray("supportedFireModes");
        Gun gun = ((GunItem) gunItem.getItem()).getModifiedGun(gunItem.getStack()); // Quick patch up, will create static method for handling null supported modes
        float dist =
                (Math.abs(event.getPlayer().moveForward)/4+
                        Math.abs(event.getPlayer().moveStrafing)/1.5f)*
                        (event.getPlayer().moveVertical != 0 ? 3:1);
        if(dist != 0)
            SyncedPlayerData.instance().set(event.getPlayer(), ModSyncedDataKeys.MOVING, dist);
        else
            SyncedPlayerData.instance().set(event.getPlayer(), ModSyncedDataKeys.MOVING, 0f);
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
