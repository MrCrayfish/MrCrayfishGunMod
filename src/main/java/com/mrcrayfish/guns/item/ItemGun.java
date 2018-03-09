package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.client.event.RenderEvents;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.object.Gun;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
	public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count)
	{
		if(!gun.general.auto)
			return;

		EntityPlayer player = (EntityPlayer) entity;
		World world = player.world;

		ItemStack ammo = this.findAmmo(player, gun.projectile.type);
		if(ammo != null || player.capabilities.isCreativeMode || this.hasIgnoreAmmo(stack))
		{
			if(count % gun.general.rate == 0)
			{
				fire(world, player, ammo);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{	
		ItemStack stack = this.findAmmo(playerIn, gun.projectile.type);
		if(stack != null || playerIn.capabilities.isCreativeMode)
		{
			playerIn.setActiveHand(handIn);
			if(!gun.general.auto)
			{
				fire(worldIn, playerIn, stack);
			}
		}
		else
		{
			worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
		}
		//Minecraft.getMinecraft().getItemRenderer().resetEquippedProgress(handIn); //TODO stop reequip animation
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	private void fire(World worldIn, EntityPlayer playerIn, ItemStack ammo)
	{
		if(ammo != null || playerIn.capabilities.isCreativeMode)
		{
			worldIn.playSound(null, playerIn.getPosition(), ModSounds.getSound(gun.sounds.fire), SoundCategory.HOSTILE, 5.0F, 0.8F + itemRand.nextFloat() * 0.2F);
			if(!worldIn.isRemote)
			{
				EntityProjectile bullet = new EntityProjectile(worldIn, playerIn, gun.projectile);
				worldIn.spawnEntity(bullet);
			}
			else
			{
				if(gun.display.flash != null)
				{
					RenderEvents.drawFlash = true;
				}
				playerIn.rotationPitch -= 0.4f;
			}

			if(!playerIn.capabilities.isCreativeMode)
			{
				ammo.shrink(1);

				if(ammo.getCount() == 0)
				{
					playerIn.inventory.deleteStack(ammo);
				}
			}
		}
	}
	
	private ItemStack findAmmo(EntityPlayer player, ItemAmmo.Type type)
    {
        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND), type))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND), type))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (this.isAmmo(stack, type))
                {
                    return stack;
                }
            }
            return null;
        }
    }
	
	protected boolean isAmmo(ItemStack stack, ItemAmmo.Type type)
    {
        return stack != null && stack.getItem() == ModGuns.AMMO && stack.getItemDamage() == type.ordinal();
    }

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}

	private boolean hasIgnoreAmmo(ItemStack gun)
	{
		NBTTagCompound tag = gun.getTagCompound();
		return tag != null && tag.getBoolean("IgnoreAmmo");
	}
}
