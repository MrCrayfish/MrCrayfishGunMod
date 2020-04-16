package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.block.BlockWorkbench;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class ModBlocks
{
    public static final DeferredRegister<Block> REGISTER = new DeferredRegister<>(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    public static final RegistryObject<Block> WORKBENCH = REGISTER.register("workbench", () -> new BlockWorkbench(Block.Properties.create(Material.IRON).hardnessAndResistance(1.5F)));
}
