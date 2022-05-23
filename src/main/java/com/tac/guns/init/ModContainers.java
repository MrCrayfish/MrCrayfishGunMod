package com.tac.guns.init;

import com.tac.guns.Reference;
import com.tac.guns.common.container.AttachmentContainer;
import com.tac.guns.common.container.ColorBenchContainer;
import com.tac.guns.common.container.InspectionContainer;
import com.tac.guns.common.container.WorkbenchContainer;
import com.tac.guns.tileentity.WorkbenchTileEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ModContainers
{
    public static final DeferredRegister<ContainerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

    public static final RegistryObject<ContainerType<WorkbenchContainer>> WORKBENCH = register("workbench", (IContainerFactory<WorkbenchContainer>) (windowId, playerInventory, data) -> {
        WorkbenchTileEntity workstation = (WorkbenchTileEntity) playerInventory.player.world.getTileEntity(data.readBlockPos());
        return new WorkbenchContainer(windowId, playerInventory, workstation);
    });

    public static final RegistryObject<ContainerType<AttachmentContainer>> ATTACHMENTS = register("attachments", AttachmentContainer::new);
    
    public static final RegistryObject<ContainerType<InspectionContainer>> INSPECTION = register("inspection", InspectionContainer::new);

    public static final RegistryObject<ContainerType<ColorBenchContainer>> COLOR_BENCH = register("color_bench", ColorBenchContainer::new);

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String id, ContainerType.IFactory<T> factory)
    {
        return REGISTER.register(id, () -> new ContainerType<>(factory));
    }
}
