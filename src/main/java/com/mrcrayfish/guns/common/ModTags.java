package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> FRAGILE = tag("fragile");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Reference.MOD_ID, name));
        }
    }
}
