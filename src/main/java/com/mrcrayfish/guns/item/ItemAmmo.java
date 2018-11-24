package com.mrcrayfish.guns.item;

import com.google.gson.annotations.SerializedName;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityGrenadeStun;
import com.mrcrayfish.guns.init.ModGuns;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class ItemAmmo extends Item implements ISubItems
{
    public ItemAmmo()
    {
        this.setUnlocalizedName("ammo");
        this.setRegistryName("ammo");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(MrCrayfishGunMod.GUN_TAB);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if(stack.getItemDamage() >= Type.values().length) return super.getUnlocalizedName(stack);
        return super.getUnlocalizedName(stack) + "_" + Type.values()[stack.getItemDamage()].name;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if(isInCreativeTab(tab))
        {
            for (int i = 0; i < Type.values().length; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
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
        if(stack.getMetadata() == Type.GRENADE.ordinal() || stack.getMetadata() == Type.GRENADE_STUN.ordinal())
        {
            playerIn.setActiveHand(handIn);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        boolean isStun = stack.getMetadata() == Type.GRENADE_STUN.ordinal();
        if(!isStun && stack.getMetadata() != Type.GRENADE.ordinal())
            return;

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
            EntityGrenade grenade = isStun ? new EntityGrenadeStun(worldIn, player) : new EntityGrenade(worldIn, player);
            grenade.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, Math.min(1.0F, duration / 20F), 1.0F);
            worldIn.spawnEntity(grenade);
        }
    }

    public static ItemStack getAmmo(Type type, int amount)
    {
        amount = Math.min(amount, 64);
        return new ItemStack(ModGuns.AMMO, amount, type.ordinal());
    }

    @Override
    public NonNullList<ResourceLocation> getModels()
    {
        NonNullList<ResourceLocation> modelLocations = NonNullList.create();
        for(Type type : Type.values())
        {
            modelLocations.add(new ResourceLocation(Reference.MOD_ID, "ammo_" + type.name));
        }
        return modelLocations;
    }

    public enum Type
    {
        @SerializedName("basic")
        BASIC("basic"),
        @SerializedName("advanced")
        ADVANCED("advanced"),
        @SerializedName("shell")
        SHELL("shell"),
        @SerializedName("grenade")
        GRENADE("grenade"),
        @SerializedName("missile")
        MISSILE("missile"),
        @SerializedName("grenade_stun")
        GRENADE_STUN("grenade_stun");

        public final String name;

        Type(String name)
        {
            this.name = name;
        }

        @Nullable
        public static Type getType(String name)
        {
            for(Type type : Type.values())
            {
                if(type.name.equals(name))
                {
                    return type;
                }
            }
            return null;
        }
    }
}
