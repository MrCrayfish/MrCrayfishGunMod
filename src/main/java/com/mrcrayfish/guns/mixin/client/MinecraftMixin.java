package com.mrcrayfish.guns.mixin.client;

import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Author: MrCrayfish
 */
@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;itemUsed(Lnet/minecraft/world/InteractionHand;)V", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void beforeItemUsed(CallbackInfo ci, InteractionHand[] hands, int var2, int var3, InteractionHand hand, InputEvent.ClickInputEvent event, ItemStack stack)
    {
        if(stack.getItem() instanceof GunItem)
        {
            ci.cancel();
        }
    }
}
