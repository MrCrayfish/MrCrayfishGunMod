package com.mrcrayfish.guns.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * <p>Fired when a player reloads a gun.</p>
 *
 * @author Ocelot
 */
public class GunReloadEvent extends PlayerEvent
{
    private final ItemStack stack;

    public GunReloadEvent(PlayerEntity player, ItemStack stack)
    {
        super(player);
        this.stack = stack;
    }

    /**
     * @return The stack the player was holding when reloading the gun
     */
    public ItemStack getStack()
    {
        return stack;
    }

    /**
     * @return Whether or not this event was fired on the client side
     */
    public boolean isClient()
    {
        return this.getPlayer().getCommandSenderWorld().isClientSide();
    }

    /**
     * <p>Fired when a player is about to reload a gun.</p>
     *
     * @author Ocelot
     */
    @Cancelable
    public static class Pre extends GunReloadEvent
    {
        public Pre(PlayerEntity player, ItemStack stack)
        {
            super(player, stack);
        }
    }

    /**
     * <p>Fired after a player has started reloading a gun.</p>
     *
     * @author Ocelot
     */
    public static class Post extends GunReloadEvent
    {
        public Post(PlayerEntity player, ItemStack stack)
        {
            super(player, stack);
        }
    }
}
