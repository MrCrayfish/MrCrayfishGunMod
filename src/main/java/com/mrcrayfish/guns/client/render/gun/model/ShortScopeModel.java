package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.opengl.GL11;

/**
 * Author: MrCrayfish
 */
public class ShortScopeModel implements IOverrideModel
{
    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/effect/red_dot_reticle.png");
    private static final ResourceLocation RED_DOT_RETICLE_GLOW = new ResourceLocation(Reference.MOD_ID, "textures/effect/red_dot_reticle_glow.png");
    private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
    {
        RenderUtil.renderModel(stack, parent, matrixStack, renderTypeBuffer, light, overlay);
        
        if(isFirstPerson(transformType) && entity.equals(Minecraft.getInstance().player))
        {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, (float) ClientHandler.getGunRenderer().normalZoomProgress * 0.5F + 0.5F);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.alphaFunc(516, 0.0F);

            RenderSystem.pushMatrix();
            {
                RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
                double size = 1.4 / 16.0;
                RenderSystem.translated(-size / 2, 0.85 * 0.0625, -0.3 * 0.0625);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                Minecraft.getInstance().getTextureManager().bindTexture(VIGNETTE);
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
                buffer.pos(0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 1.0F).lightmap(light).endVertex();
                buffer.pos(size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 1.0F).lightmap(light).endVertex();
                buffer.pos(size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).lightmap(light).endVertex();
                buffer.pos(0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 0).lightmap(light).endVertex();
                tessellator.draw();

                RenderSystem.pushMatrix();
                {
                    double invertProgress = (1.0 - ClientHandler.getGunRenderer().normalZoomProgress);
                    RenderSystem.translated(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                    double scale = 6.0;
                    RenderSystem.translated(size / 2, size / 2, 0);
                    RenderSystem.translated(-(size / scale) / 2, -(size / scale) / 2, 0);
                    RenderSystem.translated(0, 0, 0.0001);

                    RenderSystem.alphaFunc(516, 0.0F);

                    int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, 0);
                    CompoundNBT tag = stack.getTag();
                    if(tag != null && tag.contains("ReticleColor", Constants.NBT.TAG_INT))
                    {
                        reticleGlowColor = tag.getInt("ReticleColor");
                    }

                    float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                    float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                    float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                    float alpha = (float) (1.0F * ClientHandler.getGunRenderer().normalZoomProgress);

                    Minecraft mc = Minecraft.getInstance();
                    mc.getTextureManager().bindTexture(RED_DOT_RETICLE_GLOW);
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
                    buffer.pos(0, size / scale, 0).color(red, green, blue, alpha).tex(0.0F, 0.9375F).lightmap(15728880).endVertex();
                    buffer.pos(0, 0, 0).color(red, green, blue, alpha).tex(0.0F, 0.0F).lightmap(15728880).endVertex();
                    buffer.pos(size / scale, 0, 0).color(red, green, blue, alpha).tex(0.9375F, 0.0F).lightmap(15728880).endVertex();
                    buffer.pos(size / scale, size / scale, 0).color(red, green, blue, alpha).tex(0.9375F, 0.9375F).lightmap(15728880).endVertex();
                    tessellator.draw();

                    alpha = (float) (0.75F * ClientHandler.getGunRenderer().normalZoomProgress);

                    mc.getTextureManager().bindTexture(RED_DOT_RETICLE);
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
                    buffer.pos(0, size / scale, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0.0F, 0.9375F).lightmap(15728880).endVertex();
                    buffer.pos(0, 0, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0.0F, 0.0F).lightmap(15728880).endVertex();
                    buffer.pos(size / scale, 0, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0.9375F, 0.0F).lightmap(15728880).endVertex();
                    buffer.pos(size / scale, size / scale, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0.9375F, 0.9375F).lightmap(15728880).endVertex();
                    tessellator.draw();

                    RenderSystem.defaultAlphaFunc();
                }
                RenderSystem.popMatrix();
            }
            RenderSystem.popMatrix();

            RenderSystem.defaultAlphaFunc();
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
        }
    }

    private boolean isFirstPerson(ItemCameraTransforms.TransformType transformType)
    {
        return transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
    }
}
