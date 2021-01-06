package com.mrcrayfish.guns.mixin.client;

import com.mrcrayfish.guns.client.Hooks;
import net.minecraft.client.MouseHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Author: MrCrayfish
 */
@Mixin(MouseHelper.class)
public class MouseHelperMixin
{
    @ModifyVariable(method = "updatePlayerLook()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 2)
    private double sensitivity(double original)
    {
        return Hooks.applyModifiedSensitivity(original);
    }
}
