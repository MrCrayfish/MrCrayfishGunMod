package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageReload;
import com.mrcrayfish.guns.network.message.MessageUnload;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: MrCrayfish
 */
public class ReloadHandler
{
    private static ReloadHandler instance;

    public static ReloadHandler get()
    {
        if(instance == null)
        {
            instance = new ReloadHandler();
        }
        return instance;
    }

    private int startReloadTick;
    private int reloadTimer;
    private int prevReloadTimer;
    private int reloadingSlot;

    private ReloadHandler() {}

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        this.prevReloadTimer = this.reloadTimer;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
            {
                if(this.reloadingSlot != player.inventory.currentItem)
                {
                    setReloading(false);
                }
            }

            updateReloadTimer(player);
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
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

    public void setReloading(boolean reloading)
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
                    if(tag != null && !tag.contains("IgnoreAmmo", Constants.NBT.TAG_BYTE))
                    {
                        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                        if(tag.getInt("AmmoCount") >= GunEnchantmentHelper.getAmmoCapacity(stack, gun))
                        {
                            return;
                        }
                        if(Gun.findAmmo(player, gun.getProjectile().getItem()).isEmpty())
                        {
                            return;
                        }
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, true);
                        PacketHandler.getPlayChannel().sendToServer(new MessageReload(true));
                        this.reloadingSlot = player.inventory.currentItem;
                    }
                }
            }
            else
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                PacketHandler.getPlayChannel().sendToServer(new MessageReload(false));
                this.reloadingSlot = -1;
            }
        }
    }

    private void updateReloadTimer(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(this.startReloadTick == -1)
            {
                this.startReloadTick = player.ticksExisted + 5;
            }
            if(this.reloadTimer < 5)
            {
                this.reloadTimer++;
            }
        }
        else
        {
            if(this.startReloadTick != -1)
            {
                this.startReloadTick = -1;
            }
            if(this.reloadTimer > 0)
            {
                this.reloadTimer--;
            }
        }
    }

    public int getStartReloadTick()
    {
        return this.startReloadTick;
    }

    public int getReloadTimer()
    {
        return this.reloadTimer;
    }

    public float getReloadProgress(float partialTicks)
    {
        return (this.prevReloadTimer + (this.reloadTimer - this.prevReloadTimer) * partialTicks) / 5F;
    }
}
