package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SMessageAim extends PlayMessage<C2SMessageAim>
{
	private boolean aiming;

	public C2SMessageAim() {}

	public C2SMessageAim(boolean aiming)
	{
		this.aiming = aiming;
	}

	@Override
	public void encode(C2SMessageAim message, FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(message.aiming);
	}

	@Override
	public C2SMessageAim decode(FriendlyByteBuf buffer)
	{
		return new C2SMessageAim(buffer.readBoolean());
	}

	@Override
	public void handle(C2SMessageAim message, Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayer player = supplier.get().getSender();
			if(player != null && !player.isSpectator())
			{
				ModSyncedDataKeys.AIMING.setValue(player, message.aiming);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
