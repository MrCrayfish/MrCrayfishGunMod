package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.common.Hooks;
import net.minecraft.server.management.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Author: MrCrayfish
 */
@Mixin(PlayerList.class)
public class PlayerListMixin
{
    @Inject(method = "reloadResources", at = @At(value = "TAIL"))
    private void onReload(CallbackInfo ci)
    {
        Hooks.onReload();
    }
}
