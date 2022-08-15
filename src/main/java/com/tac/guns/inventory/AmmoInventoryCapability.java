package com.tac.guns.inventory;

import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AmmoInventoryCapability implements ICapabilityProvider, ICapabilitySerializable<ListNBT> {

    private Capability capability = InventoryListener.ITEM_HANDLER_CAPABILITY;
    private IAmmoItemHandler itemHandler;
    private LazyOptional<IAmmoItemHandler> optionalStorage;

    public AmmoInventoryCapability(IAmmoItemHandler itemHandler) {
        this.itemHandler = itemHandler;
        this.optionalStorage = LazyOptional.of(() -> itemHandler);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == capability) {
            return optionalStorage.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public ListNBT serializeNBT() {
        return (ListNBT) capability.getStorage().writeNBT(capability, itemHandler, null);
    }

    @Override
    public void deserializeNBT(ListNBT nbt) {
        capability.getStorage().readNBT(capability, itemHandler, null, nbt);
    }

    public IAmmoItemHandler getItemHandler() {
        return itemHandler;
    }

    public LazyOptional<IAmmoItemHandler> getOptionalStorage() {
        return optionalStorage;
    }
}
