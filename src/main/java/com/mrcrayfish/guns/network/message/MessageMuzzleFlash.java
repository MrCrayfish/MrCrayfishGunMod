package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.event.RenderEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageMuzzleFlash implements IMessage, IMessageHandler<MessageMuzzleFlash, IMessage>
{
    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageMuzzleFlash message, MessageContext ctx)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        player.rotationPitch -= 0.4f;
        RenderEvents.drawFlash = true;
        return null;
    }
}
