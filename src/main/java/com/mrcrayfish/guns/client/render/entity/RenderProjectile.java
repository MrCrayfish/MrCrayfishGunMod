package com.mrcrayfish.guns.client.render.entity;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModGuns;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderProjectile extends Render<EntityProjectile> 
{
	public static final ItemStack STACK = new ItemStack(ModGuns.grenade);
	
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
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x - 0.5, y, z - 0.5);
			
			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			
			GlStateManager.disableLighting();
			
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(STACK);
			Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                this.renderQuads(buffer, model.getQuads((IBlockState)null, enumfacing, 0L), -1, STACK);
            }

            this.renderQuads(buffer, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), -1, STACK);
            tessellator.draw();
		}
		GlStateManager.popMatrix();
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, @Nullable ItemStack stack)
    {
        boolean flag = color == -1 && stack != null;
        int i = 0;

        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad quad = (BakedQuad)quads.get(i);
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, quad, -1);
        }
    }

}
