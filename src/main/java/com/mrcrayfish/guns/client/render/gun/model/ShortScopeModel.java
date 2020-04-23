package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.event.GunRenderer;
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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Author: MrCrayfish
 */
public class ShortScopeModel implements IOverrideModel
{
    private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    @Override
    public void init() {}

    @Override
    public void tick(LivingEntity entity) {}

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
            RenderSystem.bindTexture(GunRenderer.screenTextureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            RenderSystem.pushMatrix();
            {
                double size = 1.4 / 16.0;
                RenderSystem.translated(-size / 2, 0.325 * 0.0625, -0.5 * 0.0625);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                Minecraft.getInstance().getTextureManager().bindTexture(VIGNETTE);
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(0, 0, 0).tex(1.0F, 1.0F).endVertex();
                buffer.pos(size, 0, 0).tex(0, 1.0F).endVertex();
                buffer.pos(size, size, 0).tex(0, 0).endVertex();
                buffer.pos(0, size, 0).tex(1.0F, 0).endVertex();
                tessellator.draw();
            }
            RenderSystem.popMatrix();

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
        }
    }

    private boolean isFirstPerson(ItemCameraTransforms.TransformType transformType)
    {
        return transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
    }
}
