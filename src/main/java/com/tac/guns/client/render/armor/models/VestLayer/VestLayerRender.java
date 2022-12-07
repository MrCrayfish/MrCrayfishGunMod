package com.tac.guns.client.render.armor.models.VestLayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Reference;
import com.tac.guns.client.render.armor.models.LightModernArmor;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Author: MrCrayfish
 */
public class VestLayerRender<T extends PlayerEntity, M extends BipedModel<T>> extends LayerRenderer<T, M>
{
    private LightArmorTest model;

    public VestLayerRender(IEntityRenderer<T, M> renderer, LightArmorTest model)
    {
        super(renderer);
        this.model = model;
    }

   /* @Override
    public void func_225628_a_(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int p_225628_3_, T player, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_)
    {
        ItemStack backpack = Backpacked.getBackpackStack(player);
        if(backpack.getItem() instanceof BackpackItem)
        {
            ItemStack chestStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            if(chestStack.getItem() == Items.ELYTRA)
            {
                return;
            }

            stack.push();
            this.getEntityModel().setModelAttributes(this.model);
            this.model.setupAngles(this.getEntityModel());
            BackpackItem item = (BackpackItem) backpack.getItem();
            IVertexBuilder builder = ItemRenderer.func_229113_a_(renderTypeBuffer, this.model.func_228282_a_(item.getModelTexture()), false, backpack.hasEffect());
            this.model.render(stack, builder, p_225628_3_, OverlayTexture.DEFAULT_LIGHT, 1.0F, 2.0F, 2.0F, 2.0F);
            stack.pop();
        }
    }*/
   private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/armor/light_armor_1.png");
    private static final RenderType type = RenderType.makeType(Reference.MOD_ID + ":armor", DefaultVertexFormats.ENTITY, GL_QUADS, 256, false, false,
            RenderType.State.getBuilder().texture(new RenderState.TextureState(texture, false, false)).build(true));

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ItemStack chestStack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.CHEST);
        if(chestStack.getItem() == Items.ELYTRA)
        {
            return;
        }

        matrixStackIn.push();
        this.getEntityModel().setModelAttributes(this.model);
        this.model.setupAngles(this.getEntityModel());
        IVertexBuilder builder = ItemRenderer.getArmorVertexBuilder(bufferIn, type, false, false);
        this.model.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 2.0F, 2.0F, 2.0F);
        matrixStackIn.pop();

    }
}

