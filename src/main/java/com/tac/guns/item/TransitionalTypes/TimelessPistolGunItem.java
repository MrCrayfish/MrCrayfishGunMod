package com.tac.guns.item.TransitionalTypes;


import com.tac.guns.GunMod;
import com.tac.guns.common.Gun;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.GunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.Process;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;


public class TimelessPistolGunItem extends TimelessGunItem {
    public TimelessPistolGunItem(Process<Properties> properties)
    {
        super(properties1 -> properties.process(new Properties().maxStackSize(1).group(GunMod.GROUP)));
    }

    public TimelessPistolGunItem(Process<Item.Properties> properties, IGunModifier... modifiers) {
        super(properties1 -> properties.process(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)),  modifiers);
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        CompoundNBT tagCompound = stack.getTag();
        super.addInformation(stack, worldIn, tooltip, flag);
        if(tagCompound != null)
        {
            //tooltip.add((new TranslationTextComponent("info.tac.oldRifle", new TranslationTextComponent(IAttachment.Type.OLD_SCOPE.getTranslationKey())).mergeStyle(TextFormatting.GREEN)));
            tooltip.add((new TranslationTextComponent("info.tac.pistolScope", new TranslationTextComponent("MiniScope").mergeStyle(TextFormatting.BOLD)).mergeStyle(TextFormatting.LIGHT_PURPLE)));
            tooltip.add((new TranslationTextComponent("info.tac.pistolBarrel", new TranslationTextComponent("PistolBarrel").mergeStyle(TextFormatting.BOLD)).mergeStyle(TextFormatting.LIGHT_PURPLE)));
        }
    }
    public TimelessPistolGunItem() {
        this(properties -> properties);
    }
}