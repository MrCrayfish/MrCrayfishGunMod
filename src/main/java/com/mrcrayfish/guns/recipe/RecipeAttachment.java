package com.mrcrayfish.guns.recipe;

import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class RecipeAttachment extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    public RecipeAttachment()
    {
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "attachment"));
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack weapon = ItemStack.EMPTY;
        ItemStack attachment = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof GunItem)
                {
                    if(!weapon.isEmpty())
                        return false;
                    weapon = stack;
                }

                if(stack.getItem() instanceof IAttachment)
                {
                    if(!attachment.isEmpty())
                        return false;
                    attachment = stack;
                }
            }
        }

        if(!weapon.isEmpty()&& !attachment.isEmpty())
        {
            IAttachment.Type type = ((IAttachment)attachment.getItem()).getType();
            Gun gun = ((GunItem)weapon.getItem()).getGun();
            return gun.canAttachType(type);
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack gun = ItemStack.EMPTY;
        ItemStack attachment = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof GunItem)
                {
                    if(!gun.isEmpty())
                        return null;
                    gun = stack.copy();
                }

                if(stack.getItem() instanceof IAttachment)
                {
                    if(!attachment.isEmpty())
                        return null;
                    attachment = stack.copy();
                }
            }
        }

        NBTTagCompound itemTag = new NBTTagCompound();
        NBTTagCompound attachments = new NBTTagCompound();
        attachments.setTag(((IAttachment)attachment.getItem()).getType().getName(), attachment.writeToNBT(new NBTTagCompound()));
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
