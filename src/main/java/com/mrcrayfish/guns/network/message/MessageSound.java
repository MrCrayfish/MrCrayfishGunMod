package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageSound implements IMessage, IMessageHandler<MessageSound, IMessage>
{
    private SoundEvent sound;
    private SoundCategory category;
    private int posX;
    private int posY;
    private int posZ;
    private float volume;
    private float pitch;

    public MessageSound() {}

    public MessageSound(SoundEvent sound, SoundCategory category, double posX, double posY, double posZ, float volume, float pitch)
    {
        this.sound = sound;
        this.category = category;
        this.posX = (int) (posX * 8.0);
        this.posY = (int) (posY * 8.0);
        this.posZ = (int) (posZ * 8.0);
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(SoundEvent.REGISTRY.getIDForObject(this.sound));
        buf.writeInt(this.category.ordinal());
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.sound = SoundEvent.REGISTRY.getObjectById(buf.readInt());
        this.category = SoundCategory.values()[buf.readInt()];
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public IMessage onMessage(MessageSound message, MessageContext ctx)
    {
        if(GunConfig.CLIENT.sound.hitSound)
        {
            MrCrayfishGunMod.proxy.playClientSound(posX, posY, posZ, message.sound, message.category, message.volume, message.pitch);
        }
        return null;
    }
}
