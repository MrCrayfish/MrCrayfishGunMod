package com.mrcrayfish.guns.entity;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class DamageSourceProjectile extends EntityDamageSourceIndirect
{
    public DamageSourceProjectile(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn)
    {
        super(damageTypeIn, source, indirectEntityIn);
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        Entity indirectEntity = this.getTrueSource();
        ITextComponent component = indirectEntity == null ? this.damageSourceEntity.getDisplayName() : indirectEntity.getDisplayName();
        ItemStack stack = indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase) indirectEntity).getHeldItemMainhand() : ItemStack.EMPTY;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";
        if(!stack.isEmpty())
        {
            if(I18n.hasKey(s1))
            {
                return new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName(), component, getItemStackName(stack));
            }
        }
        return new TextComponentTranslation(s, entityLivingBaseIn.getDisplayName(), component);
    }

    private ITextComponent getItemStackName(ItemStack stack)
    {
        TextComponentString textComponentString = new TextComponentString(stack.getDisplayName());
        if (stack.hasDisplayName())
        {
            textComponentString.getStyle().setItalic(Boolean.TRUE);
        }
        if (!stack.isEmpty())
        {
            NBTTagCompound nbttagcompound = stack.writeToNBT(new NBTTagCompound());
            textComponentString.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(nbttagcompound.toString())));
            textComponentString.getStyle().setColor(TextFormatting.YELLOW);
        }
        return textComponentString;
    }
}
