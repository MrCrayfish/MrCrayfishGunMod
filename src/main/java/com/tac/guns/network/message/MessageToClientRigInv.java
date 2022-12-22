package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

public class MessageToClientRigInv implements IMessage
{
	public MessageToClientRigInv() {}

	private ResourceLocation id;
	public ResourceLocation getId()
	{
		return this.id;
	}

	public MessageToClientRigInv(ResourceLocation id)
	{
		this.id = id;
	}

	public void encode(PacketBuffer buffer)
	{
		buffer.writeResourceLocation(this.id);
	}

	public void decode(PacketBuffer buffer)
	{
		this.id = buffer.readResourceLocation();
	}


	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = context.get().getSender();
			if(player != null && !player.isSpectator())
			{
				ServerPlayHandler.handleRigAmmoCount(player, this.id);
			}
		});
		context.get().setPacketHandled(true);
	}
}
