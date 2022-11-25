package com.tac.guns.network.message;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.GunMod;
import com.tac.guns.common.network.ServerPlayHandler;
import com.tac.guns.init.ModSyncedDataKeys;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageArmorRepair implements IMessage
{
	private boolean repairOrCancel;
	private boolean repairApply;

	public MessageArmorRepair() {}


	/**
	 * @param repairOrCancel IS NOT NEEDED IF MESSAGE BEING USED FOR FIX APPLICATION, true = play sound, started fixing, false = cancelation
	 * @param repairApply
	 */
	public MessageArmorRepair(boolean repairOrCancel, boolean repairApply)
	{
		this.repairOrCancel = repairOrCancel;
		this.repairApply = repairApply;
	}

	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(this.repairOrCancel);
		buffer.writeBoolean(this.repairApply);
	}

	public void decode(PacketBuffer buffer)
	{
		this.repairOrCancel = buffer.readBoolean();
		this.repairApply = buffer.readBoolean();
	}

	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = supplier.get().getSender();
			if(player != null && !player.isSpectator())
			{
				if(this.repairApply)
				{
					ServerPlayHandler.handleArmorFixApplication(player);
					SyncedPlayerData.instance().set(player, ModSyncedDataKeys.QREPAIRING, false);
				}
				else
					SyncedPlayerData.instance().set(player, ModSyncedDataKeys.QREPAIRING, this.repairOrCancel);
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
