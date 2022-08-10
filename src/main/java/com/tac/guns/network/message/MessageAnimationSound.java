package com.tac.guns.network.message;

import com.tac.guns.client.render.animation.impl.AnimationMeta;
import com.tac.guns.client.render.animation.impl.AnimationSoundManager;
import com.tac.guns.client.render.animation.impl.AnimationSoundMeta;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageAnimationSound implements IMessage{
    private ResourceLocation animationResource;
    private ResourceLocation soundResource;
    private boolean play;
    private UUID exclude;

    public MessageAnimationSound(){}

    public MessageAnimationSound(ResourceLocation animationResource,
                                 ResourceLocation soundResource,
                                 boolean play,
                                 UUID exclude)
    {
        this.animationResource = animationResource;
        this.soundResource = soundResource;
        this.play = play;
        this.exclude = exclude;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(animationResource);
        buffer.writeResourceLocation(soundResource);
        buffer.writeBoolean(play);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        animationResource = buffer.readResourceLocation();
        soundResource = buffer.readResourceLocation();
        play = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() ->
        {
            PlayerEntity player = supplier.get().getSender();
            if (player == null) return;
            if (animationResource == null || soundResource == null) return;
            AnimationMeta animationMeta = new AnimationMeta(animationResource);
            AnimationSoundMeta soundMeta = new AnimationSoundMeta(soundResource);
            if (play) AnimationSoundManager.INSTANCE.playerSound(player, animationMeta, soundMeta);
            else AnimationSoundManager.INSTANCE.interruptSound(player, animationMeta);
        });
        supplier.get().setPacketHandled(true);
    }
}
