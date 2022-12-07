package com.tac.guns.client.render.armor.models.VestLayer;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

/**
 * Author: MrCrayfish
 */
public class LightArmorTest<T extends LivingEntity> extends BipedModel<T>
{
    public ModelRenderer backpack;

    public LightArmorTest()
    {
        super(0.0F);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.backpack = new ModelRenderer(this, 0, 0);
        this.backpack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.backpack.addBox(-3.5F, 1.0F, 2.0F, 7, 9, 4, 0.0F);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
       return ImmutableList.of();
    }

    /*protected Iterable<ModelRenderer> modelRenderers()
    {
        return ImmutableList.of();
    }*/

    /*@Override
    protected Iterable<ModelRenderer> func_225600_b_()
    {
        return ImmutableList.of(this.backpack);
    }*/

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.backpack);
    }

    public void setupAngles(BipedModel<T> model)
    {
        copyProperties(model.bipedBody, this.backpack);
    }

    private static void copyProperties(ModelRenderer source, ModelRenderer target)
    {
        target.copyModelAngles(source);
    }
}
