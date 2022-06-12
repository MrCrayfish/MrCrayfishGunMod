package com.mrcrayfish.guns.mixin.client;

import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author: MrCrayfish
 */
@Mixin(ItemBlockRenderTypes.class)
public class RenderTypeLookupMixin
{
    @Inject(method = "getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;", at = @At(value = "HEAD"), cancellable = true)
    private static void getRenderTypeHead(ItemStack stack, boolean entity, CallbackInfoReturnable<RenderType> cir)
    {
        if(GunRenderingHandler.get().getRenderingWeapon() != null)
        {
            cir.setReturnValue(entity ? Sheets.translucentItemSheet() : RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS));
        }
    }
}
