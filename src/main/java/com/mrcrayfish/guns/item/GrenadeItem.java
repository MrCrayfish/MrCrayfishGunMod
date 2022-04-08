package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.entity.ThrowableGrenadeEntity;
import com.mrcrayfish.guns.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class GrenadeItem extends AmmoItem
{
    protected int maxCookTime;

    public GrenadeItem(Item.Properties properties, int maxCookTime)
    {
        super(properties);
        this.maxCookTime = maxCookTime;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
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
        if(!this.canCook()) return;

        int duration = this.getUseDuration(stack) - count;
        if(duration == 10)
            player.level.playLocalSound(player.getX(), player.getY(), player.getZ(), ModSounds.ITEM_GRENADE_PIN.get(), SoundCategory.PLAYERS, 1.0F, 1.0F, false);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return ActionResult.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        if(this.canCook() && !worldIn.isClientSide())
        {
            if(!(entityLiving instanceof PlayerEntity) || !((PlayerEntity) entityLiving).isCreative())
                stack.shrink(1);
            ThrowableGrenadeEntity grenade = this.create(worldIn, entityLiving, 0);
            grenade.onDeath();
            if(entityLiving instanceof PlayerEntity)
            {
                ((PlayerEntity) entityLiving).awardStat(Stats.ITEM_USED.get(this));
            }
        }
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if(!worldIn.isClientSide())
        {
            int duration = this.getUseDuration(stack) - timeLeft;
            if(duration >= 10)
            {
                if(!(entityLiving instanceof PlayerEntity) || !((PlayerEntity) entityLiving).isCreative())
                    stack.shrink(1);
                ThrowableGrenadeEntity grenade = this.create(worldIn, entityLiving, this.maxCookTime - duration);
                grenade.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, Math.min(1.0F, duration / 20F), 1.0F);
                worldIn.addFreshEntity(grenade);
                this.onThrown(worldIn, grenade);
                if(entityLiving instanceof PlayerEntity)
                {
                    ((PlayerEntity) entityLiving).awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public ThrowableGrenadeEntity create(World world, LivingEntity entity, int timeLeft)
    {
        return new ThrowableGrenadeEntity(world, entity, timeLeft);
    }

    public boolean canCook()
    {
        return true;
    }

    protected void onThrown(World world, ThrowableGrenadeEntity entity)
    {
    }
}
