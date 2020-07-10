package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.entity.ThrowableGrenadeEntity;
import com.mrcrayfish.guns.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class GrenadeItem extends Item
{
    protected int maxCookTime;

    public GrenadeItem(Item.Properties properties, int maxCookTime)
    {
        super(properties);
        this.maxCookTime = maxCookTime;
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return this.maxCookTime;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count)
    {
        if (!this.canCook())
            return;

        int duration = this.getUseDuration(stack) - count;
        if (duration == 10)
        {
            player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ITEM_GRENADE_PIN.get(), SoundCategory.PLAYERS, 1.0F, 1.0F, false);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        if (this.canCook() && !worldIn.isRemote())
        {
            if (entityLiving instanceof PlayerEntity)
            {
                if (!((PlayerEntity) entityLiving).isCreative())
                {
                    stack.shrink(1);
                }
            }
            PlayerEntity player = (PlayerEntity) entityLiving;
            ThrowableGrenadeEntity grenade = this.create(worldIn, player, 0);
            grenade.onDeath();
        }
        return stack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            if (!((PlayerEntity) entityLiving).isCreative())
            {
                stack.shrink(1);
            }
        }
        if (entityLiving instanceof PlayerEntity)
        {
            int duration = this.getUseDuration(stack) - timeLeft;
            if (duration >= 10)
            {
                PlayerEntity player = (PlayerEntity) entityLiving;
                if (!worldIn.isRemote)
                {
                    ThrowableGrenadeEntity grenade = this.create(worldIn, player, this.maxCookTime - duration);
                    grenade.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, Math.min(1.0F, duration / 20F), 1.0F);
                    worldIn.addEntity(grenade);
                }
                if (!this.canCook())
                    player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ITEM_GRENADE_PIN.get(), SoundCategory.PLAYERS, 1.0F, 1.0F, false);
            }
        }
    }

    public ThrowableGrenadeEntity create(World world, PlayerEntity player, int timeLeft)
    {
        return new ThrowableGrenadeEntity(world, player, timeLeft);
    }

    public boolean canCook()
    {
        return true;
    }
}
