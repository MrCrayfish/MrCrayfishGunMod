package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.entity.DamageSourceProjectile;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageUpdateGuns;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * Author: MrCrayfish
 */
public class Hooks
{
    /**
     * Linked via ASM. Checks if the specified damage source can cause knockback. Unfortunately
     * Forge's event {@link net.minecraftforge.event.entity.living.LivingKnockBackEvent} does not
     * providing the damage source and it's impossible to check if the immediate damage source is
     * a projectile, it only knows about the shooter. As always, definitely not the best way to be
     * doing this and in fact should be a PR into a Forge. See knockback.js to see how this method
     * is called.
     *
     * @param source the damage source causing the knockback
     * @return true if the specified damage source can cause knockback
     */
    @SuppressWarnings("unused")
    public static boolean canCauseKnockBack(DamageSource source)
    {
        if(source instanceof DamageSourceProjectile)
        {
            return Config.COMMON.gameplay.enableKnockback.get();
        }
        return true;
    }
}
