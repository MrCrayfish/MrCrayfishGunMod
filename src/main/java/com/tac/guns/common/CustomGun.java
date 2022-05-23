package com.tac.guns.common;

import com.tac.guns.annotation.Ignored;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class CustomGun implements INBTSerializable<CompoundNBT>
{
    @Ignored
    public ItemStack model;
    public Gun gun;

    public ItemStack getModel()
    {
        return this.model;
    }

    public Gun getGun()
    {
        return this.gun;
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT compound = new CompoundNBT();
        compound.put("Model", this.model.write(new CompoundNBT()));
        compound.put("Gun", this.gun.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound)
    {
        this.model = ItemStack.read(compound.getCompound("Model"));
        this.gun = Gun.create(compound.getCompound("Gun"));
    }
}
