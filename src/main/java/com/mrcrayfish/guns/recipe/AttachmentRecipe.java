package com.mrcrayfish.guns.recipe;

import com.mrcrayfish.guns.init.ModRecipeSerializers;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class AttachmentRecipe extends SpecialRecipe
{
    public AttachmentRecipe(ResourceLocation id)
    {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn)
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
                    {
                        return false;
                    }
                    weapon = stack;
                }

                if(stack.getItem() instanceof IAttachment)
                {
                    if(!attachment.isEmpty())
                    {
                        return false;
                    }
                    attachment = stack;
                }
            }
        }

        if(!weapon.isEmpty() && !attachment.isEmpty())
        {
            IAttachment.Type type = ((IAttachment) attachment.getItem()).getType();
            Gun gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
            return gun.canAttachType(type);
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv)
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

        CompoundNBT itemTag = new CompoundNBT();
        CompoundNBT attachments = new CompoundNBT();
        attachments.put(((IAttachment)attachment.getItem()).getType().getId(), attachment.write(new CompoundNBT()));
        itemTag.put("Attachments", attachments);

        CompoundNBT gunTag = ItemStackUtil.createTagCompound(gun);
        gunTag.merge(itemTag);

        return gun;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.ATTACHMENT.get();
    }


    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

}
