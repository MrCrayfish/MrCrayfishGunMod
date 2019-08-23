package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.entity.EntityThrowableGrenade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class ItemGrenade extends ItemAmmo
{
    public ItemGrenade(ResourceLocation id)
    {
        super(id);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if(entityLiving instanceof EntityPlayer)
        {
            if(!((EntityPlayer) entityLiving).capabilities.isCreativeMode)
            {
                stack.shrink(1);
            }
        }
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer)
        {
            int duration = this.getMaxItemUseDuration(stack) - timeLeft;
            EntityPlayer player = (EntityPlayer) entityLiving;
            EntityThrowableGrenade grenade = this.create(worldIn, player);
            grenade.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, Math.min(1.0F, duration / 20F), 1.0F);
            worldIn.spawnEntity(grenade);
        }
    }

    public EntityThrowableGrenade create(World world, EntityPlayer player)
    {
        return new EntityThrowableGrenade(world, player);
    }
}
