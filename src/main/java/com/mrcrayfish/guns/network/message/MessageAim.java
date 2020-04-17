package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAim implements IMessage
{
	private boolean aiming;

	public MessageAim() {}

	public MessageAim(boolean aiming)
	{
		this.aiming = aiming;
	}

	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(this.aiming);
	}

	public void decode(PacketBuffer buffer)
	{
		this.aiming = buffer.readBoolean();
	}

	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = supplier.get().getSender();
			if(player != null && !player.isSpectator())
			{
				SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, this.aiming);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
