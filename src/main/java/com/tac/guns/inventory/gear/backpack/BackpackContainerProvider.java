package com.tac.guns.inventory.gear.backpack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class BackpackContainerProvider implements INamedContainerProvider {

    private ItemStack item;

    public BackpackContainerProvider(ItemStack item) {
        this.item = item;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("AmmoPack");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        BackpackContainer container = new BackpackContainer(windowId, inv, this.item);
        return container;
    }
}
