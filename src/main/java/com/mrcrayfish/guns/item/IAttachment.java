package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
@Beta
public interface IAttachment
{
    Type getType();

    enum Type
    {
        SCOPE, BARREL;

        public String getName()
        {
            return name().toLowerCase();
        }

        @Nullable
        public static Type getType(String s)
        {
            for(Type type : values())
            {
                if(type.getName().equals(s))
                {
                    return type;
                }
            }
            return null;
        }
    }
}
