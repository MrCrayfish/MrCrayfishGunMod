package com.tac.guns.client.render.animation.module;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AnimationMeta {
    private final ResourceLocation resourceLocation;

    public AnimationMeta(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
    }
    public ResourceLocation getResourceLocation(){
        return resourceLocation;
    }

    public boolean equals(AnimationMeta meta){
        if(meta == null) return false;
        return meta.resourceLocation.equals(resourceLocation);
    }
}
