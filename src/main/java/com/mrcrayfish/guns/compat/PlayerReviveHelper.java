package com.mrcrayfish.guns.compat;

import com.mrcrayfish.guns.GunMod;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: MrCrayfish
 */
public class PlayerReviveHelper
{
    private static boolean disable = false;
    private static Method getBleeding;
    private static Method isBleeding;

    public static boolean isBleeding(Player player)
    {
        if(!GunMod.playerReviveLoaded || disable)
            return false;

        try
        {
            init();
            Object object = getBleeding.invoke(null, player);
            return (boolean) isBleeding.invoke(object);
        }
        catch(InvocationTargetException | IllegalAccessException e)
        {
            disable = true;
        }
        return false;
    }

    private static void init()
    {
        if(getBleeding == null)
        {
            try
            {
                Class<?> playerReviveServer = Class.forName("team.creative.playerrevive.server.PlayerReviveServer");
                getBleeding = playerReviveServer.getDeclaredMethod("getBleeding", Player.class);
                Class<?> bleeding = Class.forName("team.creative.playerrevive.cap.Bleeding");
                isBleeding = bleeding.getDeclaredMethod("isBleeding");
            }
            catch(ClassNotFoundException | NoSuchMethodException ignored)
            {
                disable = true;
            }
        }
    }
}
