package com.tac.guns.item;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.init.ModSounds;
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
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GrenadeItem extends AmmoItem
{
    protected int maxCookTime;
    private float power;
    private float speed;

    public GrenadeItem(Item.Properties properties, int maxCookTime, float power, float speed)
    {
        super(properties);
        this.maxCookTime = maxCookTime;
        this.power = power;
        this.speed = speed;
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
        if(!this.canCook()) return;

        int duration = this.getUseDuration(stack) - count;
        if(duration == 5)
            player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ITEM_GRENADE_PIN.get(), SoundCategory.PLAYERS, 1.0F, 1.0F, false);
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
        if(this.canCook() && !worldIn.isRemote())
        {
            if(!(entityLiving instanceof PlayerEntity) || !((PlayerEntity) entityLiving).isCreative())
                stack.shrink(1);
            ThrowableGrenadeEntity grenade = this.create(worldIn, entityLiving, 0);
            grenade.onDeath();
        }
        return stack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if(!worldIn.isRemote())
        {
            int duration = this.getUseDuration(stack) - timeLeft;
            if(duration >= 5)
            {
                if(!(entityLiving instanceof PlayerEntity) || !((PlayerEntity) entityLiving).isCreative())
                    stack.shrink(1);
                ThrowableGrenadeEntity grenade = this.create(worldIn, entityLiving, this.maxCookTime - duration);
                grenade.func_234612_a_(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, Math.min(1.0F, duration / 20F)*this.speed, 1.5F);
                worldIn.addEntity(grenade);
                this.onThrown(worldIn, grenade);
            }
        }
    }

    public ThrowableGrenadeEntity create(World world, LivingEntity entity, int timeLeft)
    {
        return null;
    }

    /*return new ThrowableGrenadeEntity(world, entity, timeLeft, this.power);*/

    public boolean canCook()
    {
        return true;
    }

    protected void onThrown(World world, ThrowableGrenadeEntity entity)
    {
    }
}
