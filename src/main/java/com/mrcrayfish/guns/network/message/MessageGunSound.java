package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.ClientHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageGunSound implements IMessage
{
    private ResourceLocation id;
    private SoundCategory category;
    private float x;
    private float y;
    private float z;
    private float volume;
    private float pitch;
    private boolean shooter;

    public MessageGunSound() {}

    public MessageGunSound(SoundEvent sound, SoundCategory category, float x, float y, float z, float volume, float pitch, boolean shooter)
    {
        this.id = sound.getRegistryName();
        this.category = category;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
        this.shooter = shooter;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeString(this.id.toString());
        buffer.writeEnumValue(this.category);
        buffer.writeFloat(this.x);
        buffer.writeFloat(this.y);
        buffer.writeFloat(this.z);
        buffer.writeFloat(this.volume);
        buffer.writeFloat(this.pitch);
        buffer.writeBoolean(this.shooter);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.id = ResourceLocation.tryCreate(buffer.readString());
        this.category = buffer.readEnumValue(SoundCategory.class);
        this.x = buffer.readFloat();
        this.y = buffer.readFloat();
        this.z = buffer.readFloat();
        this.volume = buffer.readFloat();
        this.pitch = buffer.readFloat();
        this.shooter = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientHandler.handleMessageGunSound(this));
        supplier.get().setPacketHandled(true);
    }

    public ResourceLocation getId()
    {
        return this.id;
    }

    public SoundCategory getCategory()
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

    public boolean isShooter()
    {
        return this.shooter;
    }
}
