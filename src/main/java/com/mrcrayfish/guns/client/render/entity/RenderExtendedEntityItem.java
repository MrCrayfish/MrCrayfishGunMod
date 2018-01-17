package com.mrcrayfish.guns.client.render.entity;

import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class RenderExtendedEntityItem extends RenderEntityItem
{
    private final RenderItem renderItem;
    private final Random random = new Random();

    public RenderExtendedEntityItem(RenderManager renderManagerIn, RenderItem renderItem)
    {
        super(renderManagerIn, renderItem);
        this.renderItem = renderItem;
    }

    public void renderAttachments(ItemStack stack)
    {
        if(stack.getItem() instanceof ItemGun)
        {
            Gun gun = ((ItemGun) stack.getItem()).getGun();
            if(stack.hasTagCompound())
            {
                if(stack.getTagCompound().hasKey("attachments", Constants.NBT.TAG_COMPOUND))
                {
                    NBTTagCompound attachment = stack.getTagCompound().getCompoundTag("attachments");
                    if(attachment.hasKey("scope", Constants.NBT.TAG_COMPOUND))
                    {
                        GlStateManager.pushMatrix();
                        {
                            ItemStack scope = new ItemStack(attachment.getCompoundTag("scope"));
                            IBakedModel scopeModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(scope);
                            GlStateManager.translate(gun.modules.attachments.scope.xOffset, gun.modules.attachments.scope.yOffset, gun.modules.attachments.scope.zOffset);
                            this.renderItem.renderItem(scope, scopeModel);
                        }
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }

    private int transformModelCount(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
    {
        ItemStack itemstack = itemIn.getItem();
        Item item = itemstack.getItem();

        if (item == null)
        {
            return 0;
        }
        else
        {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.getModelCount(itemstack);
            float f = 0.25F;
            float f1 = shouldBob() ? MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
            float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);

            if (flag || this.renderManager.options != null)
            {
                float f3 = (((float)itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * (180F / (float)Math.PI);
                GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    @Override
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ItemStack itemstack = entity.getItem();
        int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        this.random.setSeed((long)i);
        boolean flag = false;

        if (this.bindEntityTexture(entity))
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.renderItem.getItemModelWithOverrides(itemstack, entity.world, (EntityLivingBase)null);
        int j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
        boolean flag1 = ibakedmodel.isGui3d();

        if (!flag1)
        {
            float f3 = -0.0F * (float)(j - 1) * 0.5F;
            float f4 = -0.0F * (float)(j - 1) * 0.5F;
            float f5 = -0.09375F * (float)(j - 1) * 0.5F;
            GlStateManager.translate(f3, f4, f5);
        }

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        for (int k = 0; k < j; ++k)
        {
            if (flag1)
            {
                GlStateManager.pushMatrix();

                if (k > 0)
                {
                    float f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f9 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(shouldSpreadItems() ? f7 : 0, shouldSpreadItems() ? f9 : 0, f6);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.renderItem.renderItem(itemstack, transformedModel);
                if(itemstack.getItem() instanceof ItemGun)
                {
                    renderAttachments(itemstack);
                }
                GlStateManager.popMatrix();
            }
            else
            {
                GlStateManager.pushMatrix();

                if (k > 0)
                {
                    float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    GlStateManager.translate(f8, f10, 0.0F);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.renderItem.renderItem(itemstack, transformedModel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.09375F);
            }
        }

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);

        if (flag)
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }
    }
}
