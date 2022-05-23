package com.tac.guns.init;

import com.tac.guns.Reference;
import com.tac.guns.tileentity.FlashLightSource;
import com.tac.guns.tileentity.WorkbenchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ModTileEntities
{
    public static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<TileEntityType<WorkbenchTileEntity>> WORKBENCH = register("workbench", WorkbenchTileEntity::new, () -> new Block[]{ModBlocks.WORKBENCH.get()});
    public static final RegistryObject<TileEntityType<FlashLightSource>> LIGHT_SOURCE = register("flashlight",FlashLightSource::new, () -> new Block[]{ModBlocks.FLASHLIGHT_BLOCK.get()});

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String id, Supplier<T> factoryIn, Supplier<Block[]> validBlocksSupplier)
    {
        return REGISTER.register(id, () -> TileEntityType.Builder.create(factoryIn, validBlocksSupplier.get()).build(null));
    }
}
