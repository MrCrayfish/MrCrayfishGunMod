package com.tac.guns.network.message;

import com.google.common.collect.ImmutableMap;
import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.common.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageUpdateRigs implements IMessage, NetworkRigManager.IRigProvider
{
    private ImmutableMap<ResourceLocation, Rig> registeredRigs;
    private ImmutableMap<ResourceLocation, CustomRig> customRigs;

    public MessageUpdateRigs() {}

    @Override
    public void encode(PacketBuffer buffer)
    {
        Validate.notNull(NetworkRigManager.get());
        Validate.notNull(CustomRigLoader.get());
        NetworkRigManager.get().writeRegisteredRigs(buffer);
        CustomRigLoader.get().writeCustomRigs(buffer);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.registeredRigs = NetworkRigManager.readRegisteredRigs(buffer);
        this.customRigs = CustomRigLoader.readCustomRigs(buffer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleUpdateRigs(this));
        supplier.get().setPacketHandled(true);
    }

    @Override
    public ImmutableMap<ResourceLocation, Rig> getRegisteredRigs()
    {
        return this.registeredRigs;
    }

    public ImmutableMap<ResourceLocation, CustomRig> getCustomRigs()
    {
        return this.customRigs;
    }
}
