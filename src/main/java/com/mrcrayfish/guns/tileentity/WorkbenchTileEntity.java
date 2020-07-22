package com.mrcrayfish.guns.tileentity;

import com.mrcrayfish.guns.common.container.WorkbenchContainer;
import com.mrcrayfish.guns.init.ModTileEntities;
import com.mrcrayfish.guns.tileentity.inventory.IStorageBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class WorkbenchTileEntity extends SyncedTileEntity implements IStorageBlock
{
    private NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    public WorkbenchTileEntity()
    {
        super(ModTileEntities.WORKBENCH.get());
    }

    @Override
    public NonNullList<ItemStack> getInventory()
    {
        return this.inventory;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        ItemStackHelper.saveAllItems(compound, this.inventory);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound)
    {
        super.read(state, compound);
        ItemStackHelper.loadAllItems(compound, this.inventory);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index != 0 || (stack.getItem() instanceof DyeItem && this.inventory.get(index).getCount() < 1);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.cgm.workbench");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new WorkbenchContainer(windowId, playerInventory, this);
    }
}
