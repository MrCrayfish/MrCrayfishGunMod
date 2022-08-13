package com.tac.guns.network.message;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.event.GunReloadEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageReload implements IMessage
{
    private boolean reload;

    public MessageReload()
    {
    }

    public MessageReload(boolean reload)
    {
        this.reload = reload;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.reload);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.reload = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null && !player.isSpectator())
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, this.reload); // This has to be set in order to verify the packet is sent if the event is cancelled
                if(!this.reload)
                    return;

                ItemStack gun = player.getHeldItemMainhand();
                if(MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, gun)))
                {
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    return;
                }
                MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Post(player, gun));

                ResourceLocation reloadSound = ((GunItem)gun.getItem()).getGun().getSounds().getCock();
                if(reloadSound != null)
                {
                    MessageGunSound message = new MessageGunSound(reloadSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) player.getPosY() + 1.0F, (float) player.getPosZ(), 1.0F, 1.0F, player.getEntityId(), false, true);
                    PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), (player.getPosY() + 1.0), player.getPosZ(), 16.0, player.world.getDimensionKey())), message);
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
