package com.tac.guns.client.render.animation.impl;

import net.minecraft.util.ResourceLocation;


public class AnimationMeta {
    private final ResourceLocation resourceLocation;

    public AnimationMeta(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
    }
    public ResourceLocation getResourceLocation(){
        return resourceLocation;
    }

    public boolean equals(AnimationMeta meta){
        return meta.resourceLocation.equals(resourceLocation);
    }
}
