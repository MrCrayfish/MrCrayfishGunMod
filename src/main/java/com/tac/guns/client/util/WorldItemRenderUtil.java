package com.tac.guns.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.FilledMapItem;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WorldItemRenderUtil extends TileEntityRenderer<UpgradeBenchTileEntity>
{
    public WorldItemRenderUtil(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    /**
     * render the tile entity - called every frame while the tileentity is in view of the player
     *
     * @param tileEntityMBE21 the associated tile entity
     * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
     *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
     *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
     * @param matrixStack     the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
     *                        it is needed for you to render the view properly.
     * @param renderBuffers    the buffer that you should render your model to
     * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
     * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
     *                        Used by vanilla for (1) red tint when a living entity takes damage, and (2) "flash" effect for creeper when ignited
     *                        CreeperRenderer.getOverlayProgress()
     */
    @Override
    public void render(UpgradeBenchTileEntity tileEntityMBE21, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                       int combinedLight, int combinedOverlay) {
        //ItemCameraTransforms.TransformType.GROUND,
        matrixStack.push();
        matrixStack.translate(0.5, 1.05, 0.5);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(90F));
        GunRenderingHandler.get().renderWeapon(Minecraft.getInstance().player, tileEntityMBE21.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND, matrixStack, renderBuffers, combinedLight, combinedOverlay);
        //RenderUtil.renderModel(RenderUtil.getModel(tileEntityMBE21.getStackInSlot(0).getItem()), tileEntityMBE21.getStackInSlot(0), matrixStack, renderBuffers, combinedLight, combinedOverlay);
        matrixStack.pop();

        // if you need to manually change the combinedLight you can use these helper functions...
        int blockLight = LightTexture.getLightBlock(combinedLight);
        int skyLight = LightTexture.getLightSky(combinedLight);
        int repackedValue = LightTexture.packLight(blockLight, skyLight);
    }

    // this should be true for tileentities which render globally (no render bounding box), such as beacons.
    @Override
    public boolean isGlobalRenderer(UpgradeBenchTileEntity tileEntityMBE21)
    {
        return false;
    }
}
