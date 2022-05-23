package com.tac.guns.mixin.common;

import com.tac.guns.entity.ProjectileEntity;
import net.minecraft.entity.boss.dragon.phase.SittingPhase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mixin(SittingPhase.class)
public class SittingPhaseMixin
{
    @Inject(method = "func_221113_a", at = @At(value = "HEAD"), cancellable = true)
    public void sittingPhaseMixin(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        if (source.getImmediateSource() instanceof ProjectileEntity) {
            cir.setReturnValue(0.0F);
        }
    }
}