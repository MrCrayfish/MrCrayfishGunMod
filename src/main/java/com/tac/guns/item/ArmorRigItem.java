package com.tac.guns.item;

import com.tac.guns.Reference;
import com.tac.guns.inventory.gear.armor.AmmoPackCapabilityProvider;
import com.tac.guns.inventory.gear.armor.AmmoPackContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ArmorRigItem extends Item {

    public ArmorRigItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(world.isRemote) return super.onItemRightClick(world, player, hand);
        if(hand != Hand.MAIN_HAND) return ActionResult.resultPass(player.getHeldItem(hand));
        AmmoPackContainerProvider containerProvider = new AmmoPackContainerProvider(player.getHeldItem(hand));
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider);
        super.onItemRightClick(world, player, hand);
        return ActionResult.resultPass(player.getHeldItem(hand));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new AmmoPackCapabilityProvider();
    }
}
