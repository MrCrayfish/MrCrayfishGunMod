package com.mrcrayfish.guns.network;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketHandler
{
    public static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel playChannel;
    private static int nextMessageId = 0;

    public static void init()
    {
        playChannel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.MOD_ID, "play")).networkProtocolVersion(() -> PROTOCOL_VERSION).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();

        registerPlayMessage(MessageAim.class, MessageAim::new, LogicalSide.SERVER);
        registerPlayMessage(MessageReload.class, MessageReload::new, LogicalSide.SERVER);
        registerPlayMessage(MessageShoot.class, MessageShoot::new, LogicalSide.SERVER);
        registerPlayMessage(MessageMuzzleFlash.class, MessageMuzzleFlash::new, LogicalSide.CLIENT);
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

    public static SimpleChannel getPlayChannel()
    {
        return playChannel;
    }
}
