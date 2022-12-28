package com.tac.guns.client.handler;


import com.tac.guns.client.InputHandler;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageFireMode;

import net.minecraft.client.Minecraft;



/**
 * @author: ClumsyAlien
 */
public class FireModeSwitchEvent
{
    private static FireModeSwitchEvent instance;

    // TODO: remove this class maybe? Its function has been replaced by callback
    @Deprecated
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
    	InputHandler.FIRE_SELECT.addPressCallback( () -> {
    		if( Minecraft.getInstance().player != null )
    			PacketHandler.getPlayChannel().sendToServer( new MessageFireMode() );
    	} );
    }
}

