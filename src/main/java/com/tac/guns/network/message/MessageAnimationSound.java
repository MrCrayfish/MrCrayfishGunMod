package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageAnimationSound implements IMessage{
    private ResourceLocation animationResource;
    private ResourceLocation soundResource;
    private boolean play;
    private UUID fromWho;

    public MessageAnimationSound(){}

    public MessageAnimationSound(ResourceLocation animationResource,
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
            ClientPlayHandler.handleMessageAnimationSound(fromWho, animationResource, soundResource, play);
        });
        supplier.get().setPacketHandled(true);
    }
}
