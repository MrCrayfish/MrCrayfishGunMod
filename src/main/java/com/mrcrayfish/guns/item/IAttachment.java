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
        SCOPE("scope", "Scope"),
        BARREL("barrel", "Barrel"),
        STOCK("stock", "Stock"),
        UNDER_BARREL("under_barrel", "Under_Barrel");

        private String translationKey;
        private String tagKey;

        Type(String translationKey, String tagKey)
        {
            this.translationKey = translationKey;
            this.tagKey = tagKey;
        }

        public String getTranslationKey()
        {
            return this.translationKey;
        }

        public String getTagKey()
        {
            return this.tagKey;
        }

        @Nullable
        public static Type byTagKey(String s)
        {
            for(Type type : values())
            {
                if(type.tagKey.equalsIgnoreCase(s))
                {
                    return type;
                }
            }
            return null;
        }
    }
}
