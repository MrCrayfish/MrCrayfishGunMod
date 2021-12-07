package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageGunSound implements IMessage
{
    private ResourceLocation id;
    private SoundSource category;
    private float x;
    private float y;
    private float z;
    private float volume;
    private float pitch;
    private int shooterId;
    private boolean muzzle;
    private boolean reload;

    public MessageGunSound() {}

    public MessageGunSound(ResourceLocation id, SoundSource category, float x, float y, float z, float volume, float pitch, int shooterId, boolean muzzle, boolean reload)
    {
        this.id = id;
        this.category = category;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
        this.shooterId = shooterId;
        this.muzzle = muzzle;
        this.reload = reload;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUtf(this.id.toString());
        buffer.writeEnum(this.category);
        buffer.writeFloat(this.x);
        buffer.writeFloat(this.y);
        buffer.writeFloat(this.z);
        buffer.writeFloat(this.volume);
        buffer.writeFloat(this.pitch);
        buffer.writeInt(this.shooterId);
        buffer.writeBoolean(this.muzzle);
        buffer.writeBoolean(this.reload);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.id = ResourceLocation.tryParse(buffer.readUtf());
        this.category = buffer.readEnum(SoundSource.class);
        this.x = buffer.readFloat();
        this.y = buffer.readFloat();
        this.z = buffer.readFloat();
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
        this.shooterId = buffer.readInt();
        this.muzzle = buffer.readBoolean();
        this.reload = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleMessageGunSound(this));
        supplier.get().setPacketHandled(true);
    }

    public ResourceLocation getId()
    {
        return this.id;
    }

    public SoundSource getCategory()
    {
        return this.category;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    public float getZ()
    {
        return this.z;
    }

    public float getVolume()
    {
        return this.volume;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public int getShooterId()
    {
        return this.shooterId;
    }

    public boolean showMuzzleFlash()
    {
        return this.muzzle;
    }

    public boolean isReload()
    {
        return this.reload;
    }
}
