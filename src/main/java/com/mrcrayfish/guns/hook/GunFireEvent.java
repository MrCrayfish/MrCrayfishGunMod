package com.mrcrayfish.guns.hook;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * <p>Fired when a player shoots a gun.</p>
 *
 * @author Ocelot
 */
public class GunFireEvent extends PlayerEvent
{
    private final ItemStack stack;

    public GunFireEvent(PlayerEntity player, ItemStack stack)
    {
        super(player);
        this.stack = stack;
    }

    /**
     * @return The stack the player was holding when firing the gun
     */
    public ItemStack getStack()
    {
        return stack;
    }

    /**
     * <p>Fired when a player is about to shoot a bullet.</p>
     *
     * @author Ocelot
     */
    @Cancelable
    public static class Pre extends GunFireEvent
    {
        public Pre(PlayerEntity player, ItemStack stack)
        {
            super(player, stack);
        }
    }

    /**
     * <p>Fired after a player has shot a bullet.</p>
     *
     * @author Ocelot
     */
    public static class Post extends GunFireEvent
    {
        public Post(PlayerEntity player, ItemStack stack)
        {
            super(player, stack);
        }
    }
}
