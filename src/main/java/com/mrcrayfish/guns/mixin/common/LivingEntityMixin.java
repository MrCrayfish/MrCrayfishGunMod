package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author: MrCrayfish
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    @Unique
    private DamageSource cgm$source;

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private void capture(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
    {
        this.cgm$source = source;
    }

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
    private double modifyApplyKnockbackArgs(double original)
    {
        if(this.cgm$source.getDirectEntity() instanceof ProjectileEntity)
        {
            if(!Config.COMMON.gameplay.enableKnockback.get())
            {
                return 0;
            }

            double strength = Config.COMMON.gameplay.knockbackStrength.get();
            if(strength > 0)
            {
                return strength;
            }
        }
        return original;
    }
}
