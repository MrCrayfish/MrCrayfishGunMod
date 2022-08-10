package com.tac.guns.client.render.animation.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum AnimationSoundManager {
    INSTANCE;
    private final Map<UUID, Map<ResourceLocation, ISound> > soundsMap = new HashMap<>();

    public void playerSound(PlayerEntity player, AnimationMeta animationMeta, AnimationSoundMeta soundMeta){
        SoundEvent soundEvent = new SoundEvent(soundMeta.getResourceLocation());
        EntityTickableSound sound = new EntityTickableSound(soundEvent, SoundCategory.PLAYERS, player);
        Minecraft.getInstance().getSoundHandler().play(sound);
        Map<ResourceLocation, ISound> map = soundsMap.computeIfAbsent(player.getUniqueID(), k -> new HashMap<>());
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
