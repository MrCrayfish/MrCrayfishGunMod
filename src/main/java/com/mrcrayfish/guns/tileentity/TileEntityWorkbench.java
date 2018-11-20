package com.mrcrayfish.guns.tileentity;

import com.mrcrayfish.guns.tileentity.inventory.IStorageBlock;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

/**
 * Author: MrCrayfish
 */
public class TileEntityWorkbench extends TileEntitySynced implements IStorageBlock
{
    private NonNullList<ItemStack> inventory = NonNullList.withSize(0, ItemStack.EMPTY);

    @Override
    public NonNullList<ItemStack> getInventory()
    {
        return inventory;
    }

    @Override
    public String getName()
    {
        return "cgm.container.workbench";
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        ItemStackHelper.saveAllItems(compound, this.inventory);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        ItemStackHelper.loadAllItems(compound, this.inventory);
    }

    /*@Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index != 0 || (stack.getItem() instanceof ItemDye && inventory.get(index).getCount() < 1);
    }*/
}
