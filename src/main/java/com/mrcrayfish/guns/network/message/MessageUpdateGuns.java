package com.mrcrayfish.guns.network.message;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import com.mrcrayfish.guns.common.CustomGun;
import com.mrcrayfish.guns.common.CustomGunLoader;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.NetworkGunManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageUpdateGuns implements IMessage, NetworkGunManager.IGunProvider
{
    private ImmutableMap<ResourceLocation, Gun> registeredGuns;
    private ImmutableMap<ResourceLocation, CustomGun> customGuns;

    public MessageUpdateGuns() {}

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        Validate.notNull(NetworkGunManager.get());
        Validate.notNull(CustomGunLoader.get());
        NetworkGunManager.get().writeRegisteredGuns(buffer);
        CustomGunLoader.get().writeCustomGuns(buffer);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.registeredGuns = NetworkGunManager.readRegisteredGuns(buffer);
        this.customGuns = CustomGunLoader.readCustomGuns(buffer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleUpdateGuns(this));
        supplier.get().setPacketHandled(true);
    }

    @Override
    public ImmutableMap<ResourceLocation, Gun> getRegisteredGuns()
    {
        return this.registeredGuns;
    }

    public ImmutableMap<ResourceLocation, CustomGun> getCustomGuns()
    {
        return this.customGuns;
    }
}
