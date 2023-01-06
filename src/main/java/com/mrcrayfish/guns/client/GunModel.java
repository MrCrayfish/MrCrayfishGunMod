package com.mrcrayfish.guns.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * A wrapper for baked model to prevent custom rendering handling.
 *
 * Author: MrCrayfish
 */
public class GunModel implements BakedModel
{
    private static final GunModel INSTANCE = new GunModel();

    private BakedModel model;

    public void setModel(BakedModel model)
    {
        this.model = model;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random)
    {
        return this.model.getQuads(state, direction, random);
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return this.model.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return this.model.isGui3d();
    }

    @Override
    public boolean usesBlockLight()
    {
        return this.model.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return this.model.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides()
    {
        return this.model.getOverrides();
    }

    public static BakedModel wrap(BakedModel model)
    {
        INSTANCE.setModel(model);
        return INSTANCE;
    }

    @Override
    public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous)
    {
        return List.of(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS));
    }
}
