package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.entity.EntityThrowableGrenade;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class GrenadeItem extends AmmoItem
{
    public GrenadeItem(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if(entityLiving instanceof PlayerEntity)
        {
            if(!((PlayerEntity) entityLiving).isCreative())
            {
                stack.shrink(1);
            }
        }
        if(!worldIn.isRemote && entityLiving instanceof PlayerEntity)
        {
            int duration = this.getUseDuration(stack) - timeLeft;
            PlayerEntity player = (PlayerEntity) entityLiving;
            EntityThrowableGrenade grenade = this.create(worldIn, player);
            grenade.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, Math.min(1.0F, duration / 20F), 1.0F);
            worldIn.addEntity(grenade);
        }
    }

    public EntityThrowableGrenade create(World world, PlayerEntity player)
    {
        return new EntityThrowableGrenade(world, player);
    }
}
