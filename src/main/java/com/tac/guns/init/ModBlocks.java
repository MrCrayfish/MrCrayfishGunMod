package com.tac.guns.init;

import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.block.FlashLightBlock;
import com.tac.guns.block.WorkbenchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ModBlocks
{
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    public static final RegistryObject<Block> WORKBENCH = register("workbench", () -> new WorkbenchBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.5F))
    {
        @Override
        public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state)
        {
            Block.spawnAsEntity((World) worldIn,pos,WORKBENCH.get().getItem(worldIn,pos,state));
        }
    },true);
    public static final RegistryObject<Block> FLASHLIGHT_BLOCK = register("flashlight", () -> new FlashLightBlock(),false);

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, boolean grouped)
    {
        return register(id, blockSupplier, block1 -> new BlockItem(block1, grouped ? new Item.Properties().group(GunMod.GROUP) : new Item.Properties().group(ItemGroup.SEARCH)));
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, @Nullable Function<T, BlockItem> supplier)
    {
        T block = blockSupplier.get();
        if(supplier != null)
        {
            ModItems.REGISTER.register(id, () -> supplier.apply(block));
        }
        return ModBlocks.REGISTER.register(id, () -> block);
    }
}
