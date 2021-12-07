package com.mrcrayfish.guns.network.message;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.framework.api.network.PlayMessage;
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
public class MessageUpdateGuns extends PlayMessage<MessageUpdateGuns>
{
    private ImmutableMap<ResourceLocation, Gun> registeredGuns;
    private ImmutableMap<ResourceLocation, CustomGun> customGuns;

    public MessageUpdateGuns() {}

    @Override
    public void encode(MessageUpdateGuns message, FriendlyByteBuf buffer)
    {
        Validate.notNull(NetworkGunManager.get());
        Validate.notNull(CustomGunLoader.get());
        NetworkGunManager.get().writeRegisteredGuns(buffer);
        CustomGunLoader.get().writeCustomGuns(buffer);
    }

    @Override
    public MessageUpdateGuns decode(FriendlyByteBuf buffer)
    {
        MessageUpdateGuns message = new MessageUpdateGuns();
        message.registeredGuns = NetworkGunManager.readRegisteredGuns(buffer);
        message.customGuns = CustomGunLoader.readCustomGuns(buffer);
        return message;
    }

    @Override
    public void handle(MessageUpdateGuns message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleUpdateGuns(message));
        supplier.get().setPacketHandled(true);
    }

    public ImmutableMap<ResourceLocation, Gun> getRegisteredGuns()
    {
        return this.registeredGuns;
    }

    public ImmutableMap<ResourceLocation, CustomGun> getCustomGuns()
    {
        return this.customGuns;
    }
}
