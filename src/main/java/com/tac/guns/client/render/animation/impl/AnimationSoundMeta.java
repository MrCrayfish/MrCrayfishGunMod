package com.tac.guns.client.render.animation.impl;

import net.minecraft.util.ResourceLocation;

public class AnimationSoundMeta {
    private final ResourceLocation resourceLocation;

    public AnimationSoundMeta(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
    }
    public ResourceLocation getResourceLocation(){
        return resourceLocation;
    }

    public boolean equals(AnimationSoundMeta meta){
        return meta.resourceLocation.equals(resourceLocation);
    }
}
