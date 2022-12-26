package com.tac.guns.util;

//import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class OptifineHelper
{
    private static Boolean loaded = null;
    private static Field programIdField;

    public static boolean isLoaded()
    {
        if(loaded == null)
        {
            try
            {
                Class.forName("optifine.Installer");
                loaded = true;
            }
            catch(ClassNotFoundException e)
            {
                loaded = false;
            }
        }
        return loaded;
    }
    public static Field isSkipHands = null;
    public static boolean isRenderingFirstPersonHand()
    {
        if(isLoaded())
        {
            try
            {
                Class<?> clazz = Class.forName("net.optifine.shaders.Shaders");
                if(clazz != null && isSkipHands == null)
                {
                    isSkipHands = clazz.getDeclaredField("isHandRenderedMain");
                }
                if(isSkipHands != null)
                {
                    return ((boolean)isSkipHands.get(null));
                }
            }
            catch(Exception ignored) {}
        }
        return true;
    }

    public static boolean isShadersEnabled()
    {
        if(isLoaded())
        {
            try
            {
                Class<?> clazz = Class.forName("net.optifine.shaders.Shaders");
                if(clazz != null && programIdField == null)
                {
                    programIdField = clazz.getDeclaredField("activeProgramID");
                }
                if(programIdField != null)
                {
                    int activeProgramID = (int) programIdField.get(null);
                    return activeProgramID != 0;
                }
            }
            catch(Exception ignored) {}
        }
        return false;
    }

    public static Method setSkipHands = null;
    public static void setSkipRenderHands()
    {
        if(isLoaded())
        {
            try
            {
                Class<?> clazz = Class.forName("net.optifine.shaders.Shaders");
                if(clazz != null && setSkipHands == null)
                {
                    setSkipHands = clazz.getDeclaredMethod("setSkipRenderHands", boolean.class, boolean.class);
                }
                if(setSkipHands != null)
                {
                    setSkipHands.invoke(true, true);
                }
            }
            catch(Exception ignored) {}
        }
    }
    public static void setRSkipRenderHands()
    {
        if(isLoaded())
        {
            try
            {
                Class<?> clazz = Class.forName("net.optifine.shaders.Shaders");
                if(clazz != null && setSkipHands == null)
                {
                    setSkipHands = clazz.getDeclaredMethod("setSkipRenderHands", boolean.class, boolean.class);
                }
                if(setSkipHands != null)
                {
                    setSkipHands.invoke(false, false);
                }
            }
            catch(Exception ignored) {}
        }
    }

    public static boolean isFastRenderEnabled()
    {
        if(isLoaded())
        {
            try
            {
                Class<?> clazz = Class.forName("net.optifine.shaders.Shaders");
                if(clazz != null && programIdField == null)
                {
                    programIdField = clazz.getDeclaredField("activeProgramID");
                }
                if(programIdField != null)
                {
                    int activeProgramID = (int) programIdField.get(null);
                    return activeProgramID != 0;
                }
            }
            catch(Exception ignored) {}
        }
        return false;
    }
}
