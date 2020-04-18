package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageReload;
import com.mrcrayfish.guns.network.message.MessageUnload;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ReloadHandler
{
    public static int reloadingSlot;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
        {
            return;
        }

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null && SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(reloadingSlot != player.inventory.currentItem)
            {
                setReloading(false);
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if(Minecraft.getInstance().player == null)
        {
            return;
        }

        if(KeyBinds.KEY_RELOAD.isPressed())
        {
            if(!SyncedPlayerData.instance().get(Minecraft.getInstance().player, ModSyncedDataKeys.RELOADING))
            {
                setReloading(true);
            }
            else
            {
                setReloading(false);
            }
        }
        if(KeyBinds.KEY_UNLOAD.isPressed())
        {
            setReloading(false);
            PacketHandler.getPlayChannel().sendToServer(new MessageUnload());
        }
    }

    public static void setReloading(boolean reloading)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            if(reloading)
            {
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem)
                {
                    CompoundNBT tag = stack.getTag();
                    if(tag != null && tag.contains("IgnoreAmmo", Constants.NBT.TAG_BYTE))
                    {
                        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                        if(tag.getInt("AmmoCount") >= gun.general.maxAmmo)
                        {
                            return;
                        }
                        if(GunItem.findAmmo(player, gun.projectile.item).isEmpty())
                        {
                            return;
                        }
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, true);
                        PacketHandler.getPlayChannel().sendToServer(new MessageReload(true));
                        reloadingSlot = player.inventory.currentItem;
                    }
                }
            }
            else
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                PacketHandler.getPlayChannel().sendToServer(new MessageReload(false));
                reloadingSlot = -1;
            }
        }
    }
}
