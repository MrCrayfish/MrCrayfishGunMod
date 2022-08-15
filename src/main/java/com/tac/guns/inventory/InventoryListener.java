package com.tac.guns.inventory;

import com.tac.guns.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
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

    @CapabilityInject(IAmmoItemHandler.class)
    public static Capability<IAmmoItemHandler> ITEM_HANDLER_CAPABILITY = null;
    public static Method addSlotMethod;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoin(EntityJoinWorldEvent event) throws InvocationTargetException, IllegalAccessException {
        if(!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntity();

        if(addSlotMethod == null) {
            addSlotMethod = ObfuscationReflectionHelper.findMethod(Container.class, "func_75146_a", Slot.class);
        }
        AmmoItemStackHandler ammoItemHandler = (AmmoItemStackHandler) player.getCapability(ITEM_HANDLER_CAPABILITY).resolve().get();
        addSlotMethod.invoke(player.container, new AmmoPackSlot(ammoItemHandler, 0, 170, 84));
        addSlotMethod.invoke(player.container, new BackpackSlot(ammoItemHandler, 1, 170, 102));
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) throws InvocationTargetException, IllegalAccessException {
        if(!(event.getObject() instanceof PlayerEntity)) return;
        AmmoInventoryCapability ammoInventoryCapability = new AmmoInventoryCapability(new AmmoItemStackHandler(2));
        event.addCapability(new ResourceLocation("tac", "inventory_capability"), ammoInventoryCapability);
        event.addListener(ammoInventoryCapability.getOptionalStorage()::invalidate);
    }
}
