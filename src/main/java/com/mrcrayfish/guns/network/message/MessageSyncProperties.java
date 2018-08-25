package com.mrcrayfish.guns.network.message;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.object.ServerGun;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class MessageSyncProperties implements IMessage, IMessageHandler<MessageSyncProperties, IMessage>
{
    private Map<String, ServerGun> serverGunMap;

    public MessageSyncProperties() {}

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(GunConfig.ID_TO_GUN.size());
        GunConfig.ID_TO_GUN.forEach((s, gun) ->
        {
            ByteBufUtils.writeUTF8String(buf, s);
            buf.writeFloat(gun.projectile.damage);
            buf.writeInt(gun.general.maxAmmo);
        });
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        ImmutableMap.Builder<String, ServerGun> builder = ImmutableMap.builder();
        int size = buf.readInt();
        for(int i = 0; i < size; i++)
        {
            String id = ByteBufUtils.readUTF8String(buf);
            ServerGun gun = new ServerGun();
            gun.damage = buf.readFloat();
            gun.maxAmmo = buf.readInt();
            builder.put(id, gun);
        }
        serverGunMap = builder.build();
    }

    @Override
    public IMessage onMessage(MessageSyncProperties message, MessageContext ctx)
    {
        MrCrayfishGunMod.proxy.syncServerGunProperties(message.serverGunMap);
        return null;
    }
}
