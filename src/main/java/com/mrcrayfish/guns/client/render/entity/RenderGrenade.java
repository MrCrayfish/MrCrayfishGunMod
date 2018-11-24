package com.mrcrayfish.guns.client.render.entity;

import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityGrenadeStun;
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
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class RenderGrenade extends Render<EntityGrenade>
{
    public RenderGrenade(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityGrenade entity)
    {
        return null;
    }

    @Override
    public void doRender(EntityGrenade entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(180F, 0, 1, 0);
            GlStateManager.rotate(entityYaw, 0, 1, 0);

            float rotation = entity.prevRotation + (entity.rotation - entity.prevRotation) * partialTicks;
            GlStateManager.translate(0, 0.15, 0);
            GlStateManager.rotate(-rotation, 1, 0, 0);
            GlStateManager.translate(0, -0.15, 0);

            if(entity instanceof EntityGrenadeStun)
            {
                GlStateManager.translate(0, 0.3, 0);
                GlStateManager.rotate(-90F, 0, 0, 1);
                GlStateManager.translate(0, -((EntityGrenadeStun) entity).height / 2, 0);
            }

            GlStateManager.translate(-0.5, 0, -0.5);

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.disableLighting();

            IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(entity.getItem());
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

            for(EnumFacing enumfacing : EnumFacing.values())
            {
                this.renderQuads(buffer, model.getQuads(null, enumfacing, 0L));
            }

            this.renderQuads(buffer, model.getQuads(null, null, 0L));
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
