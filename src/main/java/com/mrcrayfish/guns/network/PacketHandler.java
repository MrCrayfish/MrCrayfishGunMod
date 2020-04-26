package com.mrcrayfish.guns.network;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketHandler
{
    public static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel handshakeChannel;
    private static SimpleChannel playChannel;
    private static int nextMessageId = 0;

    public static void init()
    {
        handshakeChannel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Reference.MOD_ID, "handshake"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        handshakeChannel.messageBuilder(HandshakeMessages.C2SAcknowledge.class, 99)
                .loginIndex(HandshakeMessages.LoginIndexedMessage::getLoginIndex, HandshakeMessages.LoginIndexedMessage::setLoginIndex)
                .decoder(HandshakeMessages.C2SAcknowledge::decode)
                .encoder(HandshakeMessages.C2SAcknowledge::encode)
                .consumer(FMLHandshakeHandler.indexFirst((handler, msg, s) -> HandshakeHandler.handleAcknowledge(msg, s)))
                .add();

        handshakeChannel.messageBuilder(HandshakeMessages.S2CUpdateGuns.class, 1)
                .loginIndex(HandshakeMessages.LoginIndexedMessage::getLoginIndex, HandshakeMessages.LoginIndexedMessage::setLoginIndex)
                .decoder(HandshakeMessages.S2CUpdateGuns::decode)
                .encoder(HandshakeMessages.S2CUpdateGuns::encode)
                .consumer(FMLHandshakeHandler.biConsumerFor((handler, msg, supplier) -> HandshakeHandler.handleUpdateGuns(msg, supplier)))
                .markAsLoginPacket()
                .add();

        playChannel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Reference.MOD_ID, "play"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();

        registerPlayMessage(MessageAim.class, MessageAim::new, LogicalSide.SERVER);
        registerPlayMessage(MessageReload.class, MessageReload::new, LogicalSide.SERVER);
        registerPlayMessage(MessageShoot.class, MessageShoot::new, LogicalSide.SERVER);
        registerPlayMessage(MessageUnload.class, MessageUnload::new, LogicalSide.SERVER);
        registerPlayMessage(MessageStunGrenade.class, MessageStunGrenade::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageCraft.class, MessageCraft::new, LogicalSide.SERVER);
        registerPlayMessage(MessageBullet.class, MessageBullet::new, LogicalSide.CLIENT);
    }

    private static <T extends IMessage> void registerPlayMessage(Class<T> clazz, Supplier<T> messageSupplier, LogicalSide side)
    {
        playChannel.registerMessage(nextMessageId++, clazz, IMessage::encode, buffer -> {
            T t = messageSupplier.get();
            t.decode(buffer);
            return t;
        }, (t, supplier) -> {
            if(supplier.get().getDirection().getReceptionSide() != side)
                throw new RuntimeException("Attempted to handle message " + clazz.getSimpleName() + " on the wrong logical side!");
            t.handle(supplier);
        });
    }

    public static SimpleChannel getHandshakeChannel()
    {
        return handshakeChannel;
    }

    public static SimpleChannel getPlayChannel()
    {
        return playChannel;
    }
}
