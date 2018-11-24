package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.GunConfig.SyncedData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageSyncProperties implements IMessage, IMessageHandler<MessageSyncProperties, IMessage>
{
    private SyncedData syncedData;

    public MessageSyncProperties()
    {
        syncedData = new SyncedData();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        syncedData.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        syncedData.fromBytes(buf);
    }

    @Override
    public IMessage onMessage(MessageSyncProperties message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> message.syncedData.syncClientToServer());
        return null;
    }
}
