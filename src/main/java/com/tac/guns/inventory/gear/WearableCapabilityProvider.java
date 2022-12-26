package com.tac.guns.inventory.gear;

import com.tac.guns.inventory.gear.armor.IAmmoItemHandler;
import com.tac.guns.inventory.gear.armor.RigSlotsHandler;
import com.tac.guns.util.WearableHelper;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WearableCapabilityProvider implements ICapabilitySerializable<ListNBT> {

    @CapabilityInject(IWearableItemHandler.class)
    public static Capability<IWearableItemHandler> capability = null;
    private GearSlotsHandler itemHandler = new GearSlotsHandler(2);
    private LazyOptional<IWearableItemHandler> optionalStorage = LazyOptional.of(() -> itemHandler);
    public LazyOptional<IWearableItemHandler> getOptionalStorage() {
        return optionalStorage;
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == capability) {
            return optionalStorage.cast();
        }
        return LazyOptional.empty();
    }

    /*public static void syncAmmoCounts(World world)
    {
        this.getUpdateTag();
        if(this.world != null && !this.world.isRemote)
        {
            if(this.world instanceof ServerWorld)
            {
                SUpdateTileEntityPacket packet = this.getUpdatePacket();
                if(packet != null)
                {
                    ServerWorld server = (ServerWorld) this.world;
                    Stream<ServerPlayerEntity> players = server.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(this.pos), false);
                    players.forEach(player -> player.connection.sendPacket(packet));
                }
            }
        }
    }*/

    @Override
    public ListNBT serializeNBT() {
        return (ListNBT) capability.getStorage().writeNBT(capability, itemHandler, null);
    }

    @Override
    public void deserializeNBT(ListNBT nbt) {
        capability.getStorage().readNBT(capability, itemHandler, null, nbt);
    }
}
