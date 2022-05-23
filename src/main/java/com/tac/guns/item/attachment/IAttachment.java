package com.tac.guns.item.attachment;

import com.tac.guns.item.attachment.impl.Attachment;

import javax.annotation.Nullable;

/**
 * The base attachment interface
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public interface IAttachment<T extends Attachment>
{
    /**
     * @return The type of this attachment
     */
    Type getType();

    /**
     * @return The additional properties about this attachment
     */
    T getProperties();

    enum Type
    {
        SCOPE("scope", "Scope"),            // 1
        BARREL("barrel", "Barrel"),            // 2
        STOCK("stock", "Stock"),            // 3
        UNDER_BARREL("under_barrel", "Under_Barrel"),            // 4
        SIDE_RAIL("side_rail", "Side_Rail"),            // 5

        OLD_SCOPE("oldScope", "OldScope"),            // 6
        PISTOL_SCOPE("pistolScope", "PistolScope"),            // 7
        PISTOL_BARREL("pistolBarrel", "PistolBarrel"),            // 8

        SCOPE_RETICLE_COLOR("reticle_color", "Reticle_Color"), // Scope Attachment Type             // 9
        SCOPE_BODY_COLOR("body_color", "Body_Color"),          // Scope Attachment Type            // 10
        SCOPE_GLASS_COLOR("glass_color", "Glass_Color");       // Scope Attachment Type            // 11
        //
        // 11 - 6 = 5

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
