package com.mrcrayfish.guns.util;

import java.lang.reflect.Field;

/**
 * Author: MrCrayfish
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
}
