package com.tac.guns.util;

import com.tac.guns.common.Rig;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.inventory.gear.GearSlotsHandler;
import com.tac.guns.inventory.gear.InventoryListener;
import com.tac.guns.item.TransitionalTypes.wearables.ArmorRigItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class WearableHelper
{
    // Helpers, to maintain speed and efficency, we need to check if the tag is populated BEFORE running the helper methods

    @Nullable
    public static ItemStack PlayerWornRig(PlayerEntity player)
    {
        GearSlotsHandler ammoItemHandler = (GearSlotsHandler) player.getCapability(InventoryListener.ITEM_HANDLER_CAPABILITY).resolve().get();
        for(ItemStack stack : ammoItemHandler.getStacks()) {
            if(stack.getItem() instanceof ArmorRigItem)
                return stack;
        }
        return null;
    }
    public static void FillDefaults(ItemStack item, Rig rig)
    {
        item.getTag().putFloat("RigDurability", RigEnchantmentHelper.getModifiedDurability(item, rig));
    }

    /**
     * @param rig The Itemstack for armor, I don't want helpers to view through static capability's
     * @return true if the armor is at full durability
     */
    public static boolean isFullDurability(ItemStack rig)
    {
        Rig modifiedRig = ((ArmorRigItem)rig.getItem()).getModifiedRig(rig);
        float max = RigEnchantmentHelper.getModifiedDurability(rig, modifiedRig);

        if(rig.getTag().getFloat("RigDurability") >= max) {
            return true;
        }
        return false;
    }

    public static boolean tickFromCurrentDurability(ItemStack rig, ProjectileEntity proj)
    {
        float og = rig.getTag().getFloat("RigDurability");
        rig.getTag().remove("RigDurability");
        if(og - proj.getDamage() > 0)
            rig.getTag().putFloat("RigDurability", og - proj.getDamage());
        else{
            rig.getTag().putFloat("RigDurability", 0);
            return true;
        }
        return false;
    }

    public static float currentDurabilityPercentage(ItemStack rig)
    {
        return rig.getTag().getFloat("RigDurability")/((ArmorRigItem) rig.getItem()).getRig().getRepair().getDurability();
    }

    /**
     * @param rig The Itemstack for armor, I don't want helpers to view through static capability's
     * @return true if the armor is fully repaired, false if armor only got ticked and not at max
     */
    public static boolean tickRepairCurrentDurability(ItemStack rig)
    {
        Rig modifiedRig = ((ArmorRigItem)rig.getItem()).getModifiedRig(rig);
        float og = rig.getTag().getFloat("RigDurability"),
                max = RigEnchantmentHelper.getModifiedDurability(rig, modifiedRig),
                ofDurability = modifiedRig.getRepair().getQuickRepairability();

        rig.getTag().remove("RigDurability");
        float totalAfterRepair = og + (max * ofDurability);
        if(totalAfterRepair >= max) {
            rig.getTag().putFloat("RigDurability", max);
        }
        else{
            rig.getTag().putFloat("RigDurability", totalAfterRepair);
            return true;
        }
        return false;
    }
    /**
     * @param rig The Itemstack for armor, I don't want helpers to view through static capability's
     * @param repair The percentage to repair off the armor, can be used for custom methods, healing stations, ETC.
     * @return true if the armor is fully repaired, false if armor only got ticked and not at max
     */
    public static boolean tickRepairCurrentDurability(ItemStack rig, float repair)
    {
        Rig modifiedRig = ((ArmorRigItem)rig.getItem()).getModifiedRig(rig);
        float og = rig.getTag().getFloat("RigDurability"),
                max = RigEnchantmentHelper.getModifiedDurability(rig, modifiedRig),
                ofDurability = repair;

        rig.getTag().remove("RigDurability");
        float totalAfterRepair = og + (max * ofDurability);
        if(og >= max) {
            rig.getTag().putFloat("RigDurability", max);
        }
        else{
            rig.getTag().putFloat("RigDurability", totalAfterRepair);
            return true;
        }
        return false;
    }
    public static float GetCurrentDurability(ItemStack item)
    {
        return item.getTag().getFloat("RigDurability");
    }
}
