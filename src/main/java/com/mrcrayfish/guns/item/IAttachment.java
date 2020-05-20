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
        SCOPE("Scope"), BARREL("Barrel");

        private String id;

        Type(String id)
        {
            this.id = id;
        }

        public String getId()
        {
            return this.id;
        }

        @Nullable
        public static Type getType(String s)
        {
            for(Type type : values())
            {
                if(type.id.equalsIgnoreCase(s))
                {
                    return type;
                }
            }
            return null;
        }
    }
}
