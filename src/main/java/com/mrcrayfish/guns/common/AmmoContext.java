package com.mrcrayfish.guns.common;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public record AmmoContext(ItemStack stack, Runnable onConsume)
{
    private static final Runnable NOOP = () -> {};
    public static final AmmoContext NONE = new AmmoContext(ItemStack.EMPTY, NOOP);

    public AmmoContext(ItemStack stack, Container container) {
        this(stack, container::setChanged);
    }

    public AmmoContext(ItemStack stack) {
        this(stack, NOOP);
    }
}
