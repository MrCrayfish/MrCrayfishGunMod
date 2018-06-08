package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageReload;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Author: MrCrayfish
 */
public class ReloadHandler
{
    private static int reloadingSlot;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player != null && player.getDataManager().get(CommonEvents.RELOADING))
        {
            if(reloadingSlot != player.inventory.currentItem)
            {
                setReloading(false);
            }
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if(KeyBinds.KEY_RELOAD.isKeyDown())
        {
            if(!Minecraft.getMinecraft().player.getDataManager().get(CommonEvents.RELOADING))
            {
                setReloading(true);
                reloadingSlot = Minecraft.getMinecraft().player.inventory.currentItem;
            }
        }
    }

    public static void setReloading(boolean reloading)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player != null)
        {
            if(reloading)
            {
                ItemStack stack = player.getHeldItemMainhand();
                NBTTagCompound tag = stack.getTagCompound();
                if(tag != null)
                {
                    Gun gun = ((ItemGun) stack.getItem()).getGun();
                    if(tag.getInteger("AmmoCount") >= gun.general.maxAmmo)
                        return;
                    if(ItemGun.findAmmo(player, gun.projectile.type) == null)
                        return;
                    Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.RELOADING, true);
                    PacketHandler.INSTANCE.sendToServer(new MessageReload(true));
                }
            }
            else
            {
                Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.RELOADING, false);
                PacketHandler.INSTANCE.sendToServer(new MessageReload(false));
            }
        }
    }
}
