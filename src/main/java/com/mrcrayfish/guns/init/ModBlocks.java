package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.block.BlockWorkbench;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Author: MrCrayfish
 */
public class ModBlocks
{
    @GameRegistry.ObjectHolder("cgm:workbench")
    public static final Block WORKBENCH = null;

    public static void register()
    {
        registerBlock(new BlockWorkbench());
    }

    private static void registerBlock(Block block)
    {
        registerBlock(block, new ItemBlock(block));
    }

    private static void registerBlock(Block block, ItemBlock item)
    {
        if(block.getRegistryName() == null)
            throw new IllegalArgumentException("A block being registered does not have a registry name and could be successfully registered.");

        RegistrationHandler.Blocks.add(block);
        if(item != null)
        {
            item.setRegistryName(block.getRegistryName());
            RegistrationHandler.Items.add(item);
        }
    }
}
