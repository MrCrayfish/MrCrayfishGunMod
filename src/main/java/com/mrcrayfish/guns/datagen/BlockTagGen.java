package com.mrcrayfish.guns.datagen;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagGen extends BlockTagsProvider
{
    public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(ModTags.Blocks.FRAGILE)
                .addTag(Tags.Blocks.GLASS_PANES)
                .addTag(Tags.Blocks.GLASS)
                .addTag(BlockTags.CANDLES)
                .add(Blocks.LILY_PAD)
                .add(Blocks.COCOA)
                .add(Blocks.END_ROD)
                .add(Blocks.SCAFFOLDING)
                .add(Blocks.SEA_PICKLE)
                .add(Blocks.TURTLE_EGG)
                .add(Blocks.GLOWSTONE)
                .add(Blocks.SEA_LANTERN)
                .add(Blocks.ICE)
                .add(Blocks.BLUE_ICE)
                .add(Blocks.FROSTED_ICE)
                .add(Blocks.PACKED_ICE)
                .add(Blocks.BAMBOO)
                .add(Blocks.SMALL_AMETHYST_BUD)
                .add(Blocks.MEDIUM_AMETHYST_BUD)
                .add(Blocks.LARGE_AMETHYST_BUD)
                .add(Blocks.AMETHYST_CLUSTER)
                .add(Blocks.LANTERN)
                .add(Blocks.SOUL_LANTERN)
                .add(Blocks.POINTED_DRIPSTONE);
    }
}
