package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.client.event.RenderEvents;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
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
		this.setMaxDamage(gun.general.maxAmmo);
	}
	
	public Gun getGun() 
	{
		return gun;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public int getDamage(ItemStack stack)
	{
		NBTTagCompound tagCompound = createTagCompound(stack);
		return gun.general.maxAmmo - tagCompound.getInteger("AmmoCount");
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
		if(this.hasAmmo(stack) || player.capabilities.isCreativeMode)
		{
			CooldownTracker tracker = player.getCooldownTracker();
			if(!tracker.hasCooldown(stack.getItem()))
			{
				tracker.setCooldown(stack.getItem(), gun.general.rate);
				fire(player.world, player, stack);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack heldItem = playerIn.getHeldItem(handIn);
		if(this.hasAmmo(heldItem) || playerIn.capabilities.isCreativeMode)
		{
			playerIn.setActiveHand(handIn);
			if(!gun.general.auto)
			{
				CooldownTracker tracker = playerIn.getCooldownTracker();
				if(!tracker.hasCooldown(heldItem.getItem()))
				{
					tracker.setCooldown(heldItem.getItem(), gun.general.rate);
					fire(worldIn, playerIn, heldItem);
				}
			}
		}
		else
		{
			worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.8F);
		}
		return new ActionResult<>(EnumActionResult.FAIL, heldItem);
	}

	private void fire(World worldIn, EntityPlayer playerIn, ItemStack heldItem)
	{
		worldIn.playSound(null, playerIn.getPosition(), ModSounds.getSound(gun.sounds.fire), SoundCategory.HOSTILE, 5.0F, 0.8F + itemRand.nextFloat() * 0.2F);
		if(!worldIn.isRemote)
		{
			EntityProjectile bullet = new EntityProjectile(worldIn, playerIn, gun.projectile);
			worldIn.spawnEntity(bullet);
		}
		else
		{
			if(gun.display.flash != null && playerIn.equals(Minecraft.getMinecraft().player))
			{
				RenderEvents.drawFlash = true;
			}
			playerIn.rotationPitch -= 0.4f;
		}

		if(!worldIn.isRemote && !playerIn.capabilities.isCreativeMode)
		{
			NBTTagCompound tag = createTagCompound(heldItem);
			if(!tag.getBoolean("IgnoreAmmo"))
			{
				tag.setInteger("AmmoCount", Math.max(0, tag.getInteger("AmmoCount") - 1));
			}
			heldItem.damageItem(1, playerIn);
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

	private boolean hasAmmo(ItemStack gunStack)
	{
		NBTTagCompound tag = createTagCompound(gunStack);
		return tag.getBoolean("IgnoreAmmo") || tag.getInteger("AmmoCount") > 0;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
		{
			ItemStack stack = new ItemStack(this);
			createTagCompound(stack).setInteger("AmmoCount", gun.general.maxAmmo);
			items.add(stack);
		}
	}

	private NBTTagCompound createTagCompound(ItemStack stack)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}
}
