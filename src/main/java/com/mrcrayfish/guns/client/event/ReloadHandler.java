package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageReload;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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
        Minecraft.getMinecraft().player.getDataManager().set(CommonEvents.RELOADING, reloading);
        PacketHandler.INSTANCE.sendToServer(new MessageReload(reloading));
    }
}
