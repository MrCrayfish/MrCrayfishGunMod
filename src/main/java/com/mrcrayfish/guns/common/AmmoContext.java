package com.mrcrayfish.guns.common;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public record AmmoContext(ItemStack stack, @Nullable Container container)
{
    public static final AmmoContext NONE = new AmmoContext(ItemStack.EMPTY, null);
}
