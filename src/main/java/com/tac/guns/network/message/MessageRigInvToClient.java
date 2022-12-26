package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.common.network.ServerPlayHandler;
import com.tac.guns.inventory.gear.InventoryListener;
import com.tac.guns.inventory.gear.armor.ArmorRigInventoryCapability;
import com.tac.guns.util.WearableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRigInvToClient implements IMessage
{
	public MessageRigInvToClient() {}

	private int count;
	public int getCount()
	{
		return this.count;
	}

	private boolean onlyResetRigCount = false;
	public boolean getOnlyResetRigCount()
	{
		return this.onlyResetRigCount;
	}
	public MessageRigInvToClient(boolean reset) {this.onlyResetRigCount = reset;}
	public MessageRigInvToClient(ItemStack rig, ResourceLocation id)
	{
		this.count = Gun.ammoCountInRig(rig, id);
	}

	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(this.count);
	}

	public void decode(PacketBuffer buffer)
	{
		this.count = buffer.readInt();
	}


	public void handle(Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() ->
		{
				ClientPlayHandler.updateRigInv(this);
		});
		ctx.setPacketHandled(true);
	}
}
