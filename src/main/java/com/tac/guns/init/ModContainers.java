package com.tac.guns.init;

import com.tac.guns.Reference;
import com.tac.guns.common.container.*;
import com.tac.guns.inventory.gear.armor.ArmorRigContainer;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import com.tac.guns.tileentity.WorkbenchTileEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Dictionary;
import java.util.HashMap;

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
    public static final RegistryObject<ContainerType<UpgradeBenchContainer>> UPGRADE_BENCH = register("upgrade_bench", (IContainerFactory<UpgradeBenchContainer>) (windowId, playerInventory, data) -> {
        UpgradeBenchTileEntity workstation = (UpgradeBenchTileEntity) playerInventory.player.world.getTileEntity(data.readBlockPos());
        return new UpgradeBenchContainer(windowId, playerInventory, workstation);
    });
    public static final RegistryObject<ContainerType<ArmorRigContainer>> ARMOR_TEST = REGISTER.register("armor_test", () -> IForgeContainerType.create((windowId, inv, data) -> new ArmorRigContainer(windowId, inv)));

    // ITEM -> CONTAINER
    /*public static final HashMap<RegistryObject, RegistryObject> containerVitem = new HashMap()
    {{
        put(ARMOR_TEST.get(), ModItems.ARMOR_TEST.get());
    }};*/

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String id, ContainerType.IFactory<T> factory)
    {
        return REGISTER.register(id, () -> new ContainerType<>(factory));
    }
}
