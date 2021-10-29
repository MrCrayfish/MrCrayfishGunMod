package com.tac.guns.client.handler;


import com.tac.guns.Reference;
import com.tac.guns.client.KeyBinds;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageFireMode;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;



/**
 * Author: ClumsyAlien
 */

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireModeSwitchEvent
{
    private static FireModeSwitchEvent instance;

    public static FireModeSwitchEvent get()
    {
        if(instance == null)
        {
            instance = new FireModeSwitchEvent();
        }
        return instance;
    }
    private FireModeSwitchEvent()
    {
    }
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if(Minecraft.getInstance().player == null)
        {
            return;
        }
        if(KeyBinds.KEY_FIRESELECT.isPressed() && event.getAction() == GLFW.GLFW_PRESS)
        {
            PacketHandler.getPlayChannel().sendToServer(new MessageFireMode());
        }
    }

}

