package com.tac.guns.client.render.animation;

import net.minecraft.util.ResourceLocation;


public class AnimationMeta {
    private final ResourceLocation resourceLocation;

    public AnimationMeta(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
    }
    public ResourceLocation getResourceLocation(){
        return resourceLocation;
    }

}
