package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.CustomGun;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class CustomGunManager
{
    private static Map<ResourceLocation, CustomGun> customGunMap;

    public static boolean updateCustomGuns(NetworkGunManager.IGunProvider provider)
    {
        CustomGunManager.customGunMap = provider.getCustomGuns();
        return true;
    }

    public static void fill(NonNullList<ItemStack> items)
    {
        if(customGunMap != null)
        {
            customGunMap.forEach((id, gun) ->
            {
                ItemStack stack = new ItemStack(ModItems.PISTOL.get());
                stack.setDisplayName(new TranslationTextComponent("item." + id.getNamespace() + "." + id.getPath() + ".name"));
                CompoundNBT tag = stack.getOrCreateTag();
                tag.put("Model", gun.getModel().write(new CompoundNBT()));
                tag.put("Gun", gun.getGun().serializeNBT());
                tag.putBoolean("Custom", true);
                tag.putInt("AmmoCount", gun.getGun().getGeneral().getMaxAmmo());
                items.add(stack);
            });
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        customGunMap = null;
    }
}
