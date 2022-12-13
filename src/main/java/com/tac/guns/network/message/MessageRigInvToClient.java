package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
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
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRigInvToClient implements IMessage
{
	public MessageRigInvToClient() {}

	private CompoundNBT data;

	public CompoundNBT getData()
	{
		return this.data;
	}

	public MessageRigInvToClient(CompoundNBT nbt)
	{
		data = nbt;
	}

	public MessageRigInvToClient(ListNBT nbt)
	{
		data = new CompoundNBT();
		ListNBT dataList = new ListNBT();
		for (int i = 0; i < nbt.size(); i++)
		{
			dataList.add(nbt.getCompound(i));
		}
		this.data.put("Items", dataList);
		this.data.putInt("Size", nbt.size());
	}

	public void encode(PacketBuffer buffer)
	{
		buffer.writeCompoundTag(this.data);
	}

	public void decode(PacketBuffer buffer)
	{
		this.data = buffer.readCompoundTag();
	}


	public void handle(Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> ClientPlayHandler.updateRigInv(this));
		ctx.setPacketHandled(true);
		ctx.setPacketHandled(true);
	}
}
