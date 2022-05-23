package com.tac.guns.client.render.crosshair;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.client.handler.AimingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class TexturedCrosshair extends Crosshair
{
    protected ResourceLocation texture;
    protected boolean blend;

    public TexturedCrosshair(ResourceLocation id)
    {
        this(id, true);
    }

    public TexturedCrosshair(ResourceLocation id, boolean blend)
    {
        super(id);
        this.texture = new ResourceLocation(id.getNamespace(), "textures/crosshair/" + id.getPath() + ".png");
        this.blend = blend;
    }

    @Override
    public void render(Minecraft mc, MatrixStack stack, int windowWidth, int windowHeight, float partialTicks)
    {
        float alpha = 1.0F - (float) AimingHandler.get().getNormalisedAdsProgress();
        float size = 8.0F;
        stack.translate((windowWidth - size) / 2F, (windowHeight - size) / 2F, 0);

        mc.getTextureManager().bindTexture(this.texture);
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();

        if(this.blend)
        {
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        Matrix4f matrix = stack.getLast().getMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(matrix, 0, size, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.pos(matrix, size, size, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.pos(matrix, size, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(buffer);

        if(this.blend)
        {
            RenderSystem.defaultBlendFunc();
        }
    }
}
