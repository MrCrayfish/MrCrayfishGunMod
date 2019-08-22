package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityGrenadeStun;
import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Author: MrCrayfish
 */
public class ModEntities
{
    public static void register()
    {
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:projectile"),   EntityProjectile.class,  "cgm.projectile",   0, MrCrayfishGunMod.instance, 64, 80, true);
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:grenade"),      EntityGrenade.class,     "cgm.grenade",      1, MrCrayfishGunMod.instance, 64, 80, true);
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:grenade_stun"), EntityGrenadeStun.class, "cgm.grenade_stun", 2, MrCrayfishGunMod.instance, 64, 80, true);
    }
}
