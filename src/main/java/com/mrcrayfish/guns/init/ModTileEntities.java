package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.tileentity.TileEntityWorkbench;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Author: MrCrayfish
 */
public class ModTileEntities
{
    public static void register()
    {
        GameRegistry.registerTileEntity(TileEntityWorkbench.class, Reference.MOD_ID + ":" + "workbench");
    }
}
