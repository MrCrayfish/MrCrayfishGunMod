package com.tac.guns.common;

import com.tac.guns.annotation.Ignored;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class CustomRig implements INBTSerializable<CompoundNBT>
{
    public Rig rig;

    public Rig getRig()
    {
        return this.rig;
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT compound = new CompoundNBT();
        compound.put("Rig", this.rig.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound)
    {
        this.rig = Rig.create(compound.getCompound("Rig"));
    }
}
