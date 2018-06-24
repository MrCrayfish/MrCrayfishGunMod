package com.mrcrayfish.guns.recipe;

import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemScope;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class RecipeAttachScope extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    public RecipeAttachScope()
    {
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "attach_scope"));
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack gun = ItemStack.EMPTY;
        ItemStack scope = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof ItemGun)
                {
                    if(!gun.isEmpty()) return false;
                    gun = stack;
                }

                if(stack.getItem() instanceof ItemScope)
                {
                    if(!scope.isEmpty()) return false;
                    scope = stack;
                }
            }
        }

        return !gun.isEmpty() && !scope.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack gun = ItemStack.EMPTY;
        ItemStack scope = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof ItemGun)
                {
                    if(!gun.isEmpty())
                        return null;
                    gun = stack.copy();
                }

                if(stack.getItem() instanceof ItemScope)
                {
                    if(!scope.isEmpty())
                        return null;
                    scope = stack.copy();
                }
            }
        }

        NBTTagCompound itemTag = new NBTTagCompound();
        NBTTagCompound attachments = new NBTTagCompound();
        attachments.setTag("scope", scope.writeToNBT(new NBTTagCompound()));
        itemTag.setTag("attachments", attachments);

        NBTTagCompound gunTag = ItemStackUtil.createTagCompound(gun);
        gunTag.merge(itemTag);

        return gun;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }
}
