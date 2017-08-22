package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.object.Gun;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemGun extends Item 
{
	private final Gun gun;
	
	public ItemGun(Gun gun) 
	{
		this.gun = gun;
		this.setUnlocalizedName(gun.id);
		this.setRegistryName(gun.id);
		this.setCreativeTab(MrCrayfishGunMod.GUN_TAB);
		this.setMaxStackSize(1);
	}
	
	public Gun getGun() 
	{
		return gun;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) 
	{
		return true;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if(!gun.projectile.auto)
			return;

		World world = ((EntityPlayer) player).world;
		if(count % gun.projectile.rate == 0)
		{
			world.playSound((EntityPlayer) player, player.getPosition(), ModSounds.getSound(gun.sounds.fire), SoundCategory.PLAYERS, 1.0F, 0.8F + itemRand.nextFloat() * 0.2F);
			if(!world.isRemote)
			{
				EntityProjectile bullet = new EntityProjectile(world, player, gun.projectile);
				world.spawnEntity(bullet);
			}
			else
			{
				player.rotationPitch -= 0.4f;
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{	
		ItemStack stack = this.findAmmo(playerIn);
		if(stack != null || playerIn.capabilities.isCreativeMode)
		{
			playerIn.setActiveHand(handIn);

			worldIn.playSound(playerIn, playerIn.getPosition(), ModSounds.getSound(gun.sounds.fire), SoundCategory.PLAYERS, 1.0F, 0.8F + itemRand.nextFloat() * 0.2F);
			if(!worldIn.isRemote)
			{
				EntityProjectile bullet = new EntityProjectile(worldIn, playerIn, gun.projectile);
				worldIn.spawnEntity(bullet);
			}
			
			if(!playerIn.capabilities.isCreativeMode)
			{
				stack.shrink(1);
				
				if (stack.getCount() == 0)
	            {
					playerIn.inventory.deleteStack(stack);
	            }	
			}
		}
		else
		{
			worldIn.playSound((EntityPlayer)null, playerIn.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	private ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(itemstack))
                {
                    return itemstack;
                }
            }

            return null;
        }
    }
	
	protected boolean isAmmo(ItemStack stack)
    {
        return stack != null && stack.getItem() == ModGuns.shotgun_ammo;
    }
	
}
