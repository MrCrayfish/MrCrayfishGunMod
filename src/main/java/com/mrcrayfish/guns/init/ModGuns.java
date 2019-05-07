package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.item.*;
import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModGuns
{
    static final Map<String, ItemGun> GUNS = new HashMap<>();

    public static final Item PARTS;
    public static final Item AMMO;
    public static final Item SCOPES;
    public static final Item SILENCER;

    static
    {
        GunConfig.ID_TO_GUN.forEach((s, gun) -> GUNS.put(s, new ItemGun(gun)));
        PARTS = new ItemPart();
        AMMO = new ItemAmmo();
        SCOPES = new ItemScope();
        SILENCER = new ItemAttachment("silencer", IAttachment.Type.BARREL);
    }

    public static void register()
    {
        for(Item item : GUNS.values())
        {
            register(item);
        }
        register(PARTS);
        register(AMMO);
        register(SCOPES);
        register(SILENCER);
    }

    private static void register(Item item)
    {
        RegistrationHandler.Items.add(item);
    }

    @Nullable
    public static ItemGun getGun(String id)
    {
        return GUNS.get(id);
    }
}
