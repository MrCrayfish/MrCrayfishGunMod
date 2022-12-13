package com.tac.guns.inventory.gear;

import com.tac.guns.Reference;
import com.tac.guns.inventory.gear.armor.ArmorRigInventoryCapability;
import com.tac.guns.inventory.gear.armor.IAmmoItemHandler;
import com.tac.guns.inventory.gear.backpack.BackpackSlot;
import com.tac.guns.item.TransitionalTypes.wearables.IArmoredRigItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class InventoryListener {

    @CapabilityInject(IWearableItemHandler.class)
    public static Capability<IWearableItemHandler> ITEM_HANDLER_CAPABILITY = null;

    @CapabilityInject(IAmmoItemHandler.class)
    public static Capability<IAmmoItemHandler> RIG_HANDLER_CAPABILITY = null;
    public static Method addSlotMethod;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoin(EntityJoinWorldEvent event) throws InvocationTargetException, IllegalAccessException {
        if(!(event.getEntity() instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) event.getEntity();
        if(addSlotMethod == null) {
            addSlotMethod = ObfuscationReflectionHelper.findMethod(Container.class, "func_75146_a", Slot.class);
        }
        GearSlotsHandler wearableItemHandler = (GearSlotsHandler) player.getCapability(ITEM_HANDLER_CAPABILITY).resolve().get();
        addSlotMethod.invoke(player.container, new ArmorRigSlot(wearableItemHandler, 0, 170, 84)); // Rig
        addSlotMethod.invoke(player.container, new BackpackSlot(wearableItemHandler, 1, 170, 102)); // Backpack
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) throws InvocationTargetException, IllegalAccessException {
        if(!(event.getObject() instanceof PlayerEntity)) return;

        WearableCapabilityProvider wearableCapability = new WearableCapabilityProvider();
        event.addCapability(new ResourceLocation("tac", "inventory_capability"), wearableCapability);
        event.addListener(wearableCapability. getOptionalStorage()::invalidate);
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesStack(AttachCapabilitiesEvent<ItemStack> event) throws InvocationTargetException, IllegalAccessException {
        if(!(event.getObject().getItem() instanceof IArmoredRigItem)) return;

        ArmorRigInventoryCapability armorRigInventoryCapability = new ArmorRigInventoryCapability();
        event.addCapability(new ResourceLocation("tac", "rig_capability"), armorRigInventoryCapability);
        event.addListener(armorRigInventoryCapability.getOptionalStorage()::invalidate);
    }
}
