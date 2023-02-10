package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.GunItemStackRenderer;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.debug.Debug;
import com.mrcrayfish.guns.enchantment.EnchantmentTypes;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class GunItem extends Item implements IColored, IMeta
{
    private WeakHashMap<CompoundTag, Gun> modifiedGunCache = new WeakHashMap<>();

    private Gun gun = new Gun();

    public GunItem(Item.Properties properties)
    {
        super(properties);
    }

    public void setGun(NetworkGunManager.Supplier supplier)
    {
        this.gun = supplier.getGun();
    }

    public Gun getGun()
    {
        return this.gun;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag)
    {
        Gun modifiedGun = this.getModifiedGun(stack);

        Item ammo = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if(ammo != null)
        {
            tooltip.add(Component.translatable("info.cgm.ammo_type", Component.translatable(ammo.getDescriptionId()).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
        }

        String additionalDamageText = "";
        CompoundTag tagCompound = stack.getTag();
        if(tagCompound != null)
        {
            if(tagCompound.contains("AdditionalDamage", Tag.TAG_ANY_NUMERIC))
            {
                float additionalDamage = tagCompound.getFloat("AdditionalDamage");
                additionalDamage += GunModifierHelper.getAdditionalDamage(stack);

                if(additionalDamage > 0)
                {
                    additionalDamageText = ChatFormatting.GREEN + " +" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
                }
                else if(additionalDamage < 0)
                {
                    additionalDamageText = ChatFormatting.RED + " " + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
                }
            }
        }

        float damage = modifiedGun.getProjectile().getDamage();
        damage = GunModifierHelper.getModifiedProjectileDamage(stack, damage);
        damage = GunEnchantmentHelper.getAcceleratorDamage(stack, damage);
        tooltip.add(Component.translatable("info.cgm.damage", ChatFormatting.WHITE + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damage) + additionalDamageText).withStyle(ChatFormatting.GRAY));

        if(tagCompound != null)
        {
            if(tagCompound.getBoolean("IgnoreAmmo"))
            {
                tooltip.add(Component.translatable("info.cgm.ignore_ammo").withStyle(ChatFormatting.AQUA));
            }
            else
            {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add(Component.translatable("info.cgm.ammo", ChatFormatting.WHITE.toString() + ammoCount + "/" + GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun)).withStyle(ChatFormatting.GRAY));
            }
        }

        tooltip.add(Component.translatable("info.cgm.attachment_help", KeyBinds.KEY_ATTACHMENTS.getTranslatedKeyMessage().getString().toUpperCase(Locale.ENGLISH)).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged;
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        CompoundTag tagCompound = stack.getOrCreateTag();
        Gun modifiedGun = this.getModifiedGun(stack);
        return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInt("AmmoCount") != GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun);
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        CompoundTag tagCompound = stack.getOrCreateTag();
        Gun modifiedGun = this.getModifiedGun(stack);
        return (int) (13.0 * (tagCompound.getInt("AmmoCount") / (double) GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun)));
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return Objects.requireNonNull(ChatFormatting.YELLOW.getColor());
    }

    public Gun getModifiedGun(ItemStack stack)
    {
        CompoundTag tagCompound = stack.getTag();
        if(tagCompound != null && tagCompound.contains("Gun", Tag.TAG_COMPOUND))
        {
            return this.modifiedGunCache.computeIfAbsent(tagCompound, item ->
            {
                if(tagCompound.getBoolean("Custom"))
                {
                    return Gun.create(tagCompound.getCompound("Gun"));
                }
                else
                {
                    Gun gunCopy = this.gun.copy();
                    gunCopy.deserializeNBT(tagCompound.getCompound("Gun"));
                    return gunCopy;
                }
            });
        }
        if(GunMod.isDebugging())
        {
            return Debug.getGun(this);
        }
        return this.gun;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if(enchantment.category == EnchantmentTypes.SEMI_AUTO_GUN)
        {
            Gun modifiedGun = this.getModifiedGun(stack);
            return !modifiedGun.getGeneral().isAuto();
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return this.getMaxStackSize(stack) == 1;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 5;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return new GunItemStackRenderer();
            }
        });
    }
}
