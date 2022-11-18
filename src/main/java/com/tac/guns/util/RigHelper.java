package com.tac.guns.util;

import com.tac.guns.common.Gun;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class RigHelper
{
    // Helpers, to maintain speed and efficency, we need to check if the tag is populated BEFORE running the helper methods

    public static float getCurrentDurrability(ItemStack rig)
    {
        return rig.getTag().getFloat("RigDurability");
    }
}
