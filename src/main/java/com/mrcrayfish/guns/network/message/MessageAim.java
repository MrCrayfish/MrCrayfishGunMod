package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAim implements IMessage
{
	private boolean aiming;

	public MessageAim() {}

	public MessageAim(boolean aiming)
	{
		this.aiming = aiming;
	}

	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(this.aiming);
	}

	public void decode(FriendlyByteBuf buffer)
	{
		this.aiming = buffer.readBoolean();
	}

	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null && !player.isSpectator())
			{
				ModSyncedDataKeys.AIMING.setValue(player, this.aiming);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
