package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Author: MrCrayfish
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
    public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn)
    {
        ITextComponent textComponent = this.getEntity() == null ? this.entity.getDisplayName() : this.getEntity().getDisplayName();
        String deathKey = String.format("death.attack.%s.%s.%s", Reference.MOD_ID, this.msgId, DEATH_TYPES[RAND.nextInt(DEATH_TYPES.length)]);
        return new TranslationTextComponent(deathKey, entityLivingBaseIn.getDisplayName(), textComponent);
    }
}
