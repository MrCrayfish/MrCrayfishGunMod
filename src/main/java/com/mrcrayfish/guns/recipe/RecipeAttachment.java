package com.mrcrayfish.guns.recipe;

/**
 * Author: MrCrayfish
 */
public class RecipeAttachment
{
    /*public RecipeAttachment()
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

        CompoundNBT itemTag = new CompoundNBT();
        CompoundNBT attachments = new CompoundNBT();
        attachments.setTag(((IAttachment)attachment.getItem()).getType().getName(), attachment.writeToNBT(new CompoundNBT()));
        itemTag.setTag("attachments", attachments);

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
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }*/
}
