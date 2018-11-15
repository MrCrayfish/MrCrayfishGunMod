package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class DamageSourceProjectile extends EntityDamageSourceIndirect
{
    private static final String[] DEATH_TYPES = { "killed", "eliminated", "executed", "annihilated", "decimated" };
    private static final Random RAND = new Random();

    private ItemStack weapon = ItemStack.EMPTY;

    public DamageSourceProjectile(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn, ItemStack weapon)
    {
        super(damageTypeIn, source, indirectEntityIn);
        this.weapon = weapon;
    }

    public ItemStack getWeapon()
    {
        return weapon;
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        ITextComponent textComponent = this.getTrueSource() == null ? this.damageSourceEntity.getDisplayName() : this.getTrueSource().getDisplayName();
        String deathKey = String.format("death.attack.%s.%s.%s", Reference.MOD_ID, this.damageType, DEATH_TYPES[RAND.nextInt(DEATH_TYPES.length)]);
        return new TextComponentTranslation(deathKey, entityLivingBaseIn.getDisplayName(), textComponent);
    }
}
