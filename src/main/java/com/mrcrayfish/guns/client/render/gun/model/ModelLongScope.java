package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
public class ModelLongScope implements IOverrideModel
{
    private static final ResourceLocation RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/blocks/sniper_reticle.png");
    private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    @Override
    public void init() {}

    @Override
    public void tick(LivingEntity entity) {}

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity)
    {
        RenderUtil.renderModel(stack, parent);

        //TODO add back scope view finder
        /*if(isFirstPerson(transformType) && entity.equals(Minecraft.getInstance().player))
        {
            if(GunRenderer.screenTextureId != -1)
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, (float) ClientProxy.gunRenderer.normalZoomProgress * 0.5F + 0.5F);
                GlStateManager.enableBlend();
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GlStateManager.disableLighting();
                RenderSystem.bindTexture(GunRenderer.screenTextureId);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                double size = 1.2 / 16.0;
                double crop = 0.425;
                Minecraft mc = Minecraft.getInstance();
                int kickAmount = ClientProxy.gunRenderer.recoilAngle > 0 ? 50 : 0;
                double offset = ClientProxy.gunRenderer.recoilNormal * kickAmount * (1.0 / mc.displayHeight);
                double texU = ((mc.displayWidth - mc.displayHeight + mc.displayHeight * crop * 2.0) / 2.0) / mc.displayWidth;

                GlStateManager.pushMatrix();
                {
                    GlStateManager.translate(-size / 2, 0.0625, 3.8 * 0.0625);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(0, size, 0).tex(texU, 1.0 - crop + offset).endVertex();
                    buffer.pos(0, 0, 0).tex(texU, crop + offset).endVertex();
                    buffer.pos(size, 0, 0).tex(1.0 - texU, crop + offset).endVertex();
                    buffer.pos(size, size, 0).tex(1.0 - texU, 1.0 - crop + offset).endVertex();
                    tessellator.draw();

                    GlStateManager.color(1.0F, 1.0F, 1.0F, (float) ClientProxy.gunRenderer.normalZoomProgress * 0.8F + 0.2F);
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
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(0, 0, 0).tex(0.984375, 0.984375).endVertex();
                    buffer.pos(size, 0, 0).tex(0, 0.984375).endVertex();
                    buffer.pos(size, size, 0).tex(0, 0).endVertex();
                    buffer.pos(0, size, 0).tex(0.984375, 0).endVertex();
                    tessellator.draw();
                }
                GlStateManager.popMatrix();

                GlStateManager.enableLighting();
            }
        }*/
    }

    private boolean isFirstPerson(ItemCameraTransforms.TransformType transformType)
    {
        return transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
    }
}
