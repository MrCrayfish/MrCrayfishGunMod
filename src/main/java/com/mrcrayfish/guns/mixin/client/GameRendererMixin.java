package com.mrcrayfish.guns.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModEffects;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Shadow
    private float fov;

    @Shadow
    private float oldFov;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/IProfiler;popPush(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    public void updateCameraAndRender(float partialTicks, long nanoTime, boolean renderWorldIn, CallbackInfo ci)
    {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;
        if (player == null)
        {
            return;
        }

        EffectInstance effect = player.getEffect(ModEffects.BLINDED.get());
        if (effect != null)
        {
            // Render white screen-filling overlay at full alpha effect when duration is above threshold
            // When below threshold, fade to full transparency as duration approaches 0
            float percent = Math.min((effect.getDuration() / (float) Config.SERVER.alphaFadeThreshold.get()), 1);
            MainWindow window = Minecraft.getInstance().getWindow();
            AbstractGui.fill(new MatrixStack(), 0, 0, window.getScreenWidth(), window.getScreenHeight(), ((int) (percent * Config.SERVER.alphaOverlay.get() + 0.5) << 24) | 16777215);
        }
    }

    /* Fixes an issue where frustum culling of chunks is not updated when the fov changes. This is
     * not a perfect fix but works for what this mod needs. It is generally not a good idea to fix
     * vanilla bugs which is why this only runs if enabled in the config. */
    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;renderLevel(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V"))
    public void fixFrustumCulling(float partialTick, long time, MatrixStack matrixStack, CallbackInfo ci)
    {
        if(!Config.CLIENT.experimental.fixChunkFrustumCulling.get())
            return;

        // Only need to apply when zooming out and delta is big enough
        if(this.fov > this.oldFov && this.fov - this.oldFov > 0.05)
        {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.levelRenderer.needsUpdate();
        }
    }
}
