package com.mrcrayfish.guns.network.message;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.CustomGunManager;
import com.mrcrayfish.guns.common.CustomGunLoader;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.object.CustomGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
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
    public void encode(PacketBuffer buffer)
    {
        Validate.notNull(GunMod.getNetworkGunManager());
        Validate.notNull(GunMod.getCustomGunLoader());
        GunMod.getNetworkGunManager().writeRegisteredGuns(buffer);
        GunMod.getCustomGunLoader().writeCustomGuns(buffer);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.registeredGuns = NetworkGunManager.readRegisteredGuns(buffer);
        this.customGuns = CustomGunLoader.readCustomGuns(buffer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> {
            NetworkGunManager.updateRegisteredGuns(this);
            CustomGunManager.updateCustomGuns(this);
        });
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
