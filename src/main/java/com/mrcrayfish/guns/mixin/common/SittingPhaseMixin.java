package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonSittingPhase;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author: MrCrayfish
 */
@Mixin(AbstractDragonSittingPhase.class)
public class SittingPhaseMixin
{
    @Inject(method = "onHurt", at = @At(value = "HEAD"), cancellable = true)
    public void sittingPhaseMixin(DamageSource source, float damage, CallbackInfoReturnable<Float> cir)
    {
        if(source.getDirectEntity() instanceof ProjectileEntity)
        {
            cir.setReturnValue(0.0F);
        }
    }
}
