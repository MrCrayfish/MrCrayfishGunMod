package com.tac.guns.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*@Mixin(InventoryScreen.class)*/
public class InventoryScreenMixin {

    /*@Inject(at = @At("TAIL"), method = "drawGuiContainerBackgroundLayer(Lcom/mojang/blaze3d/matrix/MatrixStack;FII)V")
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y, CallbackInfo ci) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("tac", "textures/gui/ammoslots.png"));
        ((InventoryScreen)(Object)this).blit(matrixStack, ((InventoryScreen)(Object)this).getGuiLeft() + 169, ((InventoryScreen)(Object)this).getGuiTop() + 78, 0, 0, 23, 46);
    }*/
}
