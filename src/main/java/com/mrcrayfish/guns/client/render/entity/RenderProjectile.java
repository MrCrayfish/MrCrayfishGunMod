package com.mrcrayfish.guns.client.render.entity;

import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderProjectile extends Render<EntityProjectile>
{
    public RenderProjectile(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.25F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityProjectile entity)
    {
        return null;
    }

    @Override
    public void doRender(EntityProjectile entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if(!entity.getProjectile().visible)
            return;

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(180F, 0, 1, 0);
            GlStateManager.rotate(entityYaw, 0, 1, 0);
            GlStateManager.rotate(entity.rotationPitch, 1, 0, 0);
            float distancePercent = (entity.ticksExisted + partialTicks) / (entity.getProjectile().life / 2);
            double translate = -0.25 - (0.25 * distancePercent);
            GlStateManager.translate(translate, -0.1, -0.5);

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.disableLighting();

            IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(entity.getItem());
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

            for(EnumFacing enumfacing : EnumFacing.values())
            {
                this.renderQuads(buffer, model.getQuads((IBlockState) null, enumfacing, 0L));
            }

            this.renderQuads(buffer, model.getQuads((IBlockState) null, (EnumFacing) null, 0L));
            tessellator.draw();

            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private void renderQuads(BufferBuilder buffer, List<BakedQuad> quads)
    {
        int i = 0;
        for(int j = quads.size(); i < j; ++i)
        {
            BakedQuad quad = quads.get(i);
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer, quad, -1);
        }
    }
}
