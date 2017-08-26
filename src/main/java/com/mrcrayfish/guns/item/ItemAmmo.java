package com.mrcrayfish.guns.item;

import com.google.gson.annotations.SerializedName;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.init.ModGuns;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class ItemAmmo extends Item
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
        return super.getUnlocalizedName(stack) + "_" + Type.values()[stack.getItemDamage()].name;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        for (int i = 0; i < Type.values().length; ++i)
        {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }

    public static ItemStack getAmmo(Type type, int amount)
    {
        amount = Math.min(amount, 64);
        return new ItemStack(ModGuns.ammo, amount, type.ordinal());
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
        MISSILE("missile");

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
