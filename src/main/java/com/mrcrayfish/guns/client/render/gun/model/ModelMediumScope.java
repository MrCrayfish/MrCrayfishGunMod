package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.event.RenderEvents;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Author: MrCrayfish
 */
public class ModelMediumScope implements IOverrideModel
{
    private static final ResourceLocation RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/blocks/sniper_reticle.png");
    private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    @Override
    public void init() {}

    @Override
    public void tick(EntityLivingBase entity) {}

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, EntityLivingBase entity)
    {
        if(RenderEvents.shadersEnabled && isFirstPerson(transformType) && entity.equals(Minecraft.getMinecraft().player))
        {
            GlStateManager.translate(0, 0, 0.15 * ClientProxy.renderEvents.normalZoomProgress);
            GlStateManager.scale(1, 1, 0.2 + 0.8 * (1.0 - ClientProxy.renderEvents.normalZoomProgress));
        }
        RenderUtil.renderModel(stack, parent);

        if(isFirstPerson(transformType) && entity.equals(Minecraft.getMinecraft().player))
        {
            if(!RenderEvents.shadersEnabled && RenderEvents.screenTextureId != -1)
            {
                RenderUtil.applyTransformType(stack, transformType);
                GlStateManager.color(1.0F, 1.0F, 1.0F, (float) ClientProxy.renderEvents.normalZoomProgress * 0.75F + 0.25F);
                GlStateManager.enableBlend();
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GlStateManager.disableLighting();
                GlStateManager.bindTexture(RenderEvents.screenTextureId);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                double scopeSize = 2.0;
                double size = scopeSize / 16.0;
                double offset = 0.58 / 16.0;
                double crop = 0.4;
                Minecraft mc = Minecraft.getMinecraft();
                double texOffset = -5 * (1.0 / mc.displayHeight);
                int kickAmount = ClientProxy.renderEvents.recoilAngle > 0 ? 50 : 5;
                texOffset += ClientProxy.renderEvents.recoilNormal * kickAmount * (1.0 / mc.displayHeight);
                double texU = ((mc.displayWidth - mc.displayHeight + mc.displayHeight * crop * 2.0) / 2.0) / mc.displayWidth;
                double texScaleX = (1.0 - texU * 2) / scopeSize;
                double texScaleY = (1.0 - crop * 2) / scopeSize;

                GlStateManager.pushMatrix();
                {
                    GlStateManager.translate(-size / 2, 1.1 * 0.0625, 1.5 * 0.0625);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(offset, 0, 0).tex(texU + 0.58 * texScaleX, crop + texOffset).endVertex();
                    buffer.pos(size - offset, 0, 0).tex(1.0 - texU - 0.58 * texScaleX, crop + texOffset).endVertex();
                    buffer.pos(size, offset, 0).tex(1.0 - texU, crop + texOffset + 0.58 * texScaleY).endVertex();
                    buffer.pos(size, size - offset, 0).tex(1.0 - texU, 1.0 - crop + texOffset - 0.58 * texScaleY).endVertex();
                    buffer.pos(size - offset, size, 0).tex(1.0 - texU - 0.58 * texScaleX, 1.0 - crop + texOffset).endVertex();
                    buffer.pos(offset, size, 0).tex(texU + 0.58 * texScaleX, 1.0 - crop + texOffset).endVertex();
                    buffer.pos(0, size - offset, 0).tex(texU, 1.0 - crop + texOffset - 0.58 * texScaleY).endVertex();
                    buffer.pos(0, offset, 0).tex(texU, crop + texOffset + 0.58 * texScaleY).endVertex();
                    tessellator.draw();

                    GlStateManager.color(1.0F, 1.0F, 1.0F, (float) ClientProxy.renderEvents.normalZoomProgress * 0.8F + 0.2F);
                    GlStateManager.translate(0, 0, 0.0001);
                    mc.getTextureManager().bindTexture(RETICLE);
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(0, 0, 0).tex(0.984375, 0.984375).endVertex();
                    buffer.pos(size, 0, 0).tex(0, 0.984375).endVertex();
                    buffer.pos(size, size, 0).tex(0, 0).endVertex();
                    buffer.pos(0, size, 0).tex(0.984375, 0).endVertex();
                    tessellator.draw();

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.translate(0, 0, 0.0001);
                    mc.getTextureManager().bindTexture(VIGNETTE);
                    buffer.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(offset, 0, 0).tex(0.58 / 2, 1.0).endVertex();
                    buffer.pos(size - offset, 0, 0).tex(1.0 - 0.58 / 2, 1.0).endVertex();
                    buffer.pos(size, offset, 0).tex(1.0, 1.0 - 0.58 / 2).endVertex();
                    buffer.pos(size, size - offset, 0).tex(1.0, 0.58 / 2).endVertex();
                    buffer.pos(size - offset, size, 0).tex(1.0 - 0.58 / 2, 0.0).endVertex();
                    buffer.pos(offset, size, 0).tex(0.58 / 2, 0.0).endVertex();
                    buffer.pos(0, size - offset, 0).tex(0.0, 0.58 / 2).endVertex();
                    buffer.pos(0, offset, 0).tex(0.0, 1.0 - 0.58 / 2).endVertex();
                    tessellator.draw();
                }
                GlStateManager.popMatrix();
            }
        }
    }

    private boolean isFirstPerson(ItemCameraTransforms.TransformType transformType)
    {
        return transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
    }
}
