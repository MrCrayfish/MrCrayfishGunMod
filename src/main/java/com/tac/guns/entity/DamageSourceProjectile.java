package com.tac.guns.entity;

import com.tac.guns.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class DamageSourceProjectile extends IndirectEntityDamageSource
{
    private static final String[] DEATH_TYPES = { "killed", "eliminated", "executed", "annihilated", "decimated" };
    private static final Random RAND = new Random();

    private ItemStack weapon;

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
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn)
    {
        ITextComponent textComponent = this.getTrueSource() == null ? this.damageSourceEntity.getDisplayName() : this.getTrueSource().getDisplayName();
        String deathKey = String.format("death.attack.%s.%s.%s", Reference.MOD_ID, this.damageType, DEATH_TYPES[RAND.nextInt(DEATH_TYPES.length)]);
        return new TranslationTextComponent(deathKey, entityLivingBaseIn.getDisplayName(), textComponent);
    }
}
