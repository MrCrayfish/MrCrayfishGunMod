package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.entity.EntityBullet;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModGuns.Gun;
import com.mrcrayfish.guns.init.ModSounds;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
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
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = this.findAmmo(playerIn);
		if(stack != ItemStack.EMPTY || playerIn.capabilities.isCreativeMode)
		{
			worldIn.playSound(playerIn, playerIn.getPosition(), ModSounds.getSound(gun.sounds.fire), SoundCategory.PLAYERS, 1.0F, 0.8F + itemRand.nextFloat() * 0.2F);
			if(!worldIn.isRemote)
			{
				EntityBullet bullet = new EntityBullet(worldIn, playerIn, gun);
				worldIn.spawnEntity(bullet);
			}
			else
			{
				playerIn.rotationPitch -= 2f;
			}
			
			if(!playerIn.capabilities.isCreativeMode)
			{
				stack.shrink(1);
				
				if (stack.isEmpty())
	            {
					playerIn.inventory.deleteStack(stack);
	            }	
			}
		}
		else
		{
			worldIn.playSound((EntityPlayer)null, playerIn.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
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

            return ItemStack.EMPTY;
        }
    }
	
	protected boolean isAmmo(ItemStack stack)
    {
        return stack.getItem() == ModGuns.shotgun_ammo;
    }
}
