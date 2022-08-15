package com.tac.guns.extra_events;

import com.tac.guns.Reference;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.init.ModSounds;
import com.tac.guns.item.TransitionalTypes.M1GunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/*
 * Author: Bomb787
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GarandPingEvent {
	/* BTW this was by bomb787 as a Timeless Contributor */
	@SubscribeEvent
    public static void postShoot(GunFireEvent.Post event) {
		PlayerEntity player = event.getPlayer();
		ItemStack heldItem = player.getHeldItemMainhand();
		if(!(heldItem.getItem() instanceof M1GunItem))
			return;
		CompoundNBT tag = heldItem.getTag();
		if(tag != null)
		{
			if(tag.getInt("AmmoCount") == 1)
				event.getPlayer().getEntityWorld().playSound(player, player.getPosition(), ModSounds.M1_PING.get()/*.GARAND_PING.get()*/, SoundCategory.MASTER, 3.0F, 1.0F);
		}
    }

}