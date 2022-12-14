package com.tac.guns.client.render.armor.VestLayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Config;
import com.tac.guns.client.render.armor.models.ModernArmor;
import com.tac.guns.item.TransitionalTypes.wearables.IArmoredRigItem;
import com.tac.guns.util.WearableHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Inspiration from MrCrayfish backpack code, my biggest problem was understanding RenderTypes and why I should learn more about them. This file is still a test however, all vest items must still
 * have a way to change their textures, along with each vest being a separate render setup rather then render-er.
 *
 * TODO: Enable for eventual bot system, this is for Biped, not just player models...
 */
@OnlyIn(Dist.CLIENT)
public class VestLayerRender<T extends PlayerEntity, M extends BipedModel<T>> extends LayerRenderer<T, M>
{
    public VestLayerRender(IEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    private static final Map<Item, ArmorBase> MODELS = new HashMap<>();

    public synchronized static <T extends ArmorBase> void registerModel(Item item, T model)
    {
        MODELS.putIfAbsent(item, model);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int p_225628_3_, T player, float p_225628_5_, float p_225628_6_, float partialTick, float p_225628_8_, float p_225628_9_, float p_225628_10_)
    {
        // Could this be too slow? The amount of checks is looks a bit, insane for high performance?
        if(WearableHelper.PlayerWornRig(player) != null && Config.COMMON.gameplay.renderTaCArmor.get())
        {
            ItemStack armor = WearableHelper.PlayerWornRig(player);
            stack.push();
            {
                Item modelName = armor.getItem();
                ArmorBase model = MODELS.get(modelName);
                if(model == null)
                {
                    model = new ModernArmor();
                }
                model.rotateToPlayerBody(this.getEntityModel().bipedBody); // Default rotation, keep for now? It's a global, maybe force more work on the model implementation side rather then core?
                IVertexBuilder builder = ItemRenderer.getBuffer(renderTypeBuffer, model.getRenderType(model.getTexture()), false, false);
                model.render(stack, builder, p_225628_3_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            stack.pop();
        }
    }
}

