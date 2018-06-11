package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageMuzzleFlash;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;

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

		if(!player.world.isRemote)
			return;

		if(ItemGun.hasAmmo(stack) || player.capabilities.isCreativeMode)
		{
			CooldownTracker tracker = player.getCooldownTracker();
			if(!tracker.hasCooldown(stack.getItem()))
			{
				tracker.setCooldown(stack.getItem(), gun.general.rate);
				PacketHandler.INSTANCE.sendToServer(new MessageShoot());
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack heldItem = playerIn.getHeldItem(handIn);
		if(worldIn.isRemote)
		{
			if(ItemGun.hasAmmo(heldItem) || playerIn.capabilities.isCreativeMode)
			{
				if(playerIn.isHandActive())
				{
					return new ActionResult<>(EnumActionResult.FAIL, heldItem);
				}
				playerIn.setActiveHand(handIn);

				if(!gun.general.auto)
				{
					CooldownTracker tracker = playerIn.getCooldownTracker();
					if(!tracker.hasCooldown(heldItem.getItem()))
					{
						tracker.setCooldown(heldItem.getItem(), gun.general.rate);
						PacketHandler.INSTANCE.sendToServer(new MessageShoot());
					}
				}
			}
			else
			{
				MrCrayfishGunMod.proxy.playClientSound(SoundEvents.BLOCK_LEVER_CLICK);
			}
		}
		return new ActionResult<>(EnumActionResult.FAIL, heldItem);
	}

	public static void fire(World worldIn, EntityPlayer playerIn, ItemStack heldItem)
	{
		if(worldIn.isRemote)
			return;

		if(playerIn.getDataManager().get(CommonEvents.RELOADING))
		{
			playerIn.getDataManager().set(CommonEvents.RELOADING, false);
		}

		Gun gun = getGun(heldItem);
		EntityProjectile bullet = new EntityProjectile(worldIn, playerIn, gun.projectile);
		worldIn.spawnEntity(bullet);
		worldIn.playSound(null, playerIn.getPosition(), ModSounds.getSound(gun.sounds.fire), SoundCategory.HOSTILE, 5.0F, 0.8F + itemRand.nextFloat() * 0.2F);

		if(gun.display.flash != null)
		{
			PacketHandler.INSTANCE.sendTo(new MessageMuzzleFlash(), (EntityPlayerMP) playerIn);
		}

		if(!playerIn.capabilities.isCreativeMode)
		{
			NBTTagCompound tag = createTagCompound(heldItem);
			if(!tag.getBoolean("IgnoreAmmo"))
			{
				tag.setInteger("AmmoCount", Math.max(0, tag.getInteger("AmmoCount") - 1));
			}
		}
	}
	
	public static ItemStack findAmmo(EntityPlayer player, ItemAmmo.Type type)
    {
    	if(player.capabilities.isCreativeMode)
		{
			return new ItemStack(ModGuns.AMMO, 64, type.ordinal());
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (isAmmo(stack, type))
			{
				return stack;
			}
		}
		return null;
    }
	
	private static boolean isAmmo(ItemStack stack, ItemAmmo.Type type)
    {
        return stack != null && stack.getItem() == ModGuns.AMMO && stack.getItemDamage() == type.ordinal();
    }

	public static boolean hasAmmo(ItemStack gunStack)
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

	private static NBTTagCompound createTagCompound(ItemStack stack)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		NBTTagCompound tagCompound = createTagCompound(stack);
		return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInteger("AmmoCount") != gun.general.maxAmmo;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		NBTTagCompound tagCompound = createTagCompound(stack);
		return 1.0 - (tagCompound.getInteger("AmmoCount") / (double) gun.general.maxAmmo);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return Color.CYAN.getRGB();
	}

	@Nullable
	public static Gun getGun(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemGun)
		{
			return ((ItemGun) stack.getItem()).gun;
		}
		return null;
	}
}
