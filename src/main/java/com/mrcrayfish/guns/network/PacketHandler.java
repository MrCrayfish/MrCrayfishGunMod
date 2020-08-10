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
        registerPlayMessage(MessageBulletHole.class, MessageBulletHole::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageAttachments.class, MessageAttachments::new, LogicalSide.SERVER);
        registerPlayMessage(MessageUpdateGuns.class, MessageUpdateGuns::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageBlood.class, MessageBlood::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageShooting.class, MessageShooting::new, LogicalSide.SERVER);
        registerPlayMessage(MessageGunSound.class, MessageGunSound::new, LogicalSide.CLIENT);
        registerPlayMessage(MessageMuzzleFlash.class, MessageMuzzleFlash::new, LogicalSide.CLIENT);
    }

    /**
     * Register an {@link IMessage} to the play network channel.
     *
     * @param clazz the class of the message
     * @param messageSupplier a supplier to create an instance of the message
     * @param side the logical side this message is to be handled on
     * @param <T> inferred by first parameter, class must implement {@link IMessage}
     */
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

    /**
     * Gets the handshake network channel for MrCrayfish's Gun Mod
     */
    public static SimpleChannel getHandshakeChannel()
    {
        return handshakeChannel;
    }

    /**
     * Gets the play network channel for MrCrayfish's Gun Mod
     */
    public static SimpleChannel getPlayChannel()
    {
        return playChannel;
    }
}
