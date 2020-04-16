package com.mrcrayfish.guns.block;

import com.mrcrayfish.guns.GunMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Author: MrCrayfish
 */
public class BlockObject extends Block
{
    public BlockObject(Material material, ResourceLocation id)
    {
        this(material, material.getMaterialMapColor(), id);
    }

    public BlockObject(Material material, MapColor mapColor, ResourceLocation id)
    {
        super(material, mapColor);
        this.setTranslationKey(id.getNamespace() + "." + id.getPath());
        this.setRegistryName(id);
        this.setCreativeTab(GunMod.GROUP);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.MIDDLE_POLE;
    }
}
