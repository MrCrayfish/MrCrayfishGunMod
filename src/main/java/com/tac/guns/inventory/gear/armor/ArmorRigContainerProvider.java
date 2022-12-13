package com.tac.guns.inventory.gear.armor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class ArmorRigContainerProvider implements INamedContainerProvider {

    private ItemStack item;

    private ArmorRigContainer container;
    public ArmorRigContainerProvider(ItemStack item) {
        this.item = item;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("AmmoPack");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        this.container = new ArmorRigContainer(windowId, inv, this.item);
        return container;
    }

    public ArmorRigContainer getContainer() {
        return this.container;
    }
}
