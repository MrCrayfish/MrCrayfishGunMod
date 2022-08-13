package com.tac.guns.client.render.animation.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public enum AnimationSoundManager {
    INSTANCE;
    private final Map<UUID, Map<ResourceLocation, ISound> > soundsMap = new HashMap<>();

    public void playerSound(PlayerEntity player, AnimationMeta animationMeta, AnimationSoundMeta soundMeta){
        Map<ResourceLocation, ISound> map = soundsMap.computeIfAbsent(player.getUniqueID(), k -> new HashMap<>());
        ISound sound = map.get(animationMeta.getResourceLocation());
        if(sound == null) {
            SoundEvent soundEvent = new SoundEvent(soundMeta.getResourceLocation());
            sound = new EntityTickableSound(soundEvent, SoundCategory.PLAYERS, player);
        }
        if(Minecraft.getInstance().getSoundHandler().isPlaying(sound)) return;
        Minecraft.getInstance().getSoundHandler().play(sound);
        map.put(animationMeta.getResourceLocation(), sound);
    }

    public void interruptSound(PlayerEntity player, AnimationMeta animationMeta){
        Map<ResourceLocation, ISound> map = soundsMap.get(player.getUniqueID());
        if(map != null){
            ISound sound = map.get(animationMeta.getResourceLocation());
            if(sound != null){
                Minecraft.getInstance().getSoundHandler().stop(sound);
            }
        }
    }
}
