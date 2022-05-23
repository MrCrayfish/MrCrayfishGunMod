package com.tac.guns.client.render.crosshair;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.AimingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class TechCrosshair extends Crosshair
{
    private static final ResourceLocation TECH_CROSSHAIR = new ResourceLocation(Reference.MOD_ID, "textures/crosshair/tech.png");
    private static final ResourceLocation DOT_CROSSHAIR = new ResourceLocation(Reference.MOD_ID, "textures/crosshair/dot.png");

    private float scale;
    private float prevScale;
    private float rotation;
    private float prevRotation;

    public TechCrosshair()
    {
        super(new ResourceLocation(Reference.MOD_ID, "tech"));
    }

    @Override
    public void tick()
    {
        this.prevRotation = this.rotation;
        this.prevScale = this.scale;
        this.rotation += 4;
        this.scale *= 0.75F;
    }

    @Override
    public void onGunFired()
    {
        this.scale = 1.5F;
    }

    @Override
    public void render(Minecraft mc, MatrixStack stack, int windowWidth, int windowHeight, float partialTicks)
    {
        float alpha = 1.0F - (float) AimingHandler.get().getNormalisedAdsProgress();
        float size = 8.0F;

        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        stack.push();
        {
            Matrix4f matrix = stack.getLast().getMatrix();
            stack.translate((windowWidth - size) / 2F, (windowHeight - size) / 2F, 0);
            mc.getTextureManager().bindTexture(DOT_CROSSHAIR);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(matrix, 0, size, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.pos(matrix, size, size, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.pos(matrix, size, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.finishDrawing();
            RenderSystem.enableAlphaTest();
            WorldVertexBufferUploader.draw(buffer);
        }
        stack.pop();

        stack.push();
        {
            Matrix4f matrix = stack.getLast().getMatrix();
            stack.translate(windowWidth / 2F, windowHeight / 2F, 0);
            float scale = 1F + MathHelper.lerp(partialTicks, this.prevScale, this.scale);
            stack.scale(scale, scale, scale);
            stack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, this.prevRotation, this.rotation)));
            stack.translate(-size / 2F, -size / 2F, 0);
            mc.getTextureManager().bindTexture(TECH_CROSSHAIR);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(matrix, 0, size, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.pos(matrix, size, size, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.pos(matrix, size, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
            buffer.finishDrawing();
            RenderSystem.enableAlphaTest();
            WorldVertexBufferUploader.draw(buffer);
        }
        stack.pop();

        RenderSystem.defaultBlendFunc();
    }
}
