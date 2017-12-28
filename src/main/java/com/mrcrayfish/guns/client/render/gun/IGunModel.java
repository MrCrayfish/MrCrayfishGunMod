package com.mrcrayfish.guns.client.render.gun;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;

/**
 * Author: MrCrayfish
 */
public interface IGunModel
{
    void registerPieces();

    void tick();

    void render(float partialTicks, ItemCameraTransforms.TransformType transformType);
}
