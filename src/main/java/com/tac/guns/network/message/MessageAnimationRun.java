package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.network.PacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageAnimationRun implements IMessage{
    private ResourceLocation animationResource;
    private ResourceLocation soundResource;
    private boolean play;
    private UUID fromWho;

    public MessageAnimationRun(){}

    public MessageAnimationRun(ResourceLocation animationResource,
                                 ResourceLocation soundResource,
                                 boolean play,
                                 UUID fromWho)
    {
        this.animationResource = animationResource;
        this.soundResource = soundResource;
        this.play = play;
        this.fromWho = fromWho;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(animationResource);
        buffer.writeResourceLocation(soundResource);
        buffer.writeBoolean(play);
        buffer.writeUniqueId(fromWho);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        animationResource = buffer.readResourceLocation();
        soundResource = buffer.readResourceLocation();
        play = buffer.readBoolean();
        fromWho = buffer.readUniqueId();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() ->
        {
            MessageAnimationSound message = new MessageAnimationSound(animationResource,soundResource,play,fromWho);
            PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), message);
        });
        supplier.get().setPacketHandled(true);
    }
}
