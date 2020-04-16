package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.common.CommonEvents;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageReload implements IMessage, IMessageHandler<MessageReload, IMessage>
{
    private boolean reload;

    public MessageReload() {}

    public MessageReload(boolean reload)
    {
        this.reload = reload;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(reload);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        reload = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(MessageReload message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
        {
            ctx.getServerHandler().player.getDataManager().set(CommonEvents.RELOADING, message.reload);
        });
        return new MessageReloadAnimation();
    }
}
