package com.mrcrayfish.guns.compat;

import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContext;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;

import java.util.concurrent.atomic.AtomicReference;

public class SophisticatedHelper {
    public static AmmoContext findAmmo(Player player, ResourceLocation id)
    {
        final AtomicReference<AmmoContext> ctx = new AtomicReference<>(AmmoContext.NONE);
        PlayerInventoryProvider.get().runOnBackpacks(player, (backpack, inventoryName, identifier, slot) -> {
            final BackpackContext.Item backpackContext = new BackpackContext.Item(inventoryName, identifier, slot);
            final InventoryHandler inv = backpackContext.getBackpackWrapper(player).getInventoryHandler();
            final int size = inv.getSlots();
            for (int i = 0; i < size; i++)
            {
                final ItemStack stack = inv.getStackInSlot(i);
                if(!Gun.isAmmo(stack, id)) continue;
                int finalI = i;
                ctx.set(new AmmoContext(stack, () -> inv.onContentsChanged(finalI)));
                return true;
            }
            return true;
        });
        return ctx.get();
    }
}
