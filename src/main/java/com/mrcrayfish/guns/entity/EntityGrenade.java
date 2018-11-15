package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemAmmo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class EntityGrenade extends EntityThrowableItem
{
    public float rotation;
    public float prevRotation;

    public EntityGrenade(World worldIn)
    {
        super(worldIn);
    }

    public EntityGrenade(World world, EntityPlayer player)
    {
        super(world, player);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.GRENADE.ordinal()));
        this.setMaxLife(20 * 3);
        this.setSize(0.25F, 0.25F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        prevRotation = rotation;

        float speed = (float) Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionY, 2) + Math.pow(motionZ, 2));
        if(speed > 0.1)
        {
            rotation += speed * 50;
        }
    }

    @Override
    public void onDeath()
    {
        world.createExplosion(thrower, posX, posY, posZ, 3.0F, true);
    }
}
