package com.tac.guns.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UpgradeBenchRenderUtil extends TileEntityRenderer<UpgradeBenchTileEntity>
{
    public UpgradeBenchRenderUtil(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
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

        matrixStack.translate(-0.14, -0.4200001, 0);
        if(!(tileEntityMBE21.getStackInSlot(0).getItem() instanceof TimelessGunItem))
            GunRenderingHandler.get().renderWeapon(Minecraft.getInstance().player, ItemStack.read(tileEntityMBE21.getUpdateTag().getCompound("weapon")), ItemCameraTransforms.TransformType.GROUND, matrixStack, renderBuffers, combinedLight, combinedOverlay);
        else
            GunRenderingHandler.get().renderWeapon(Minecraft.getInstance().player, tileEntityMBE21.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND, matrixStack, renderBuffers, combinedLight, combinedOverlay);
        matrixStack.pop();

        matrixStack.push();

        /*if(Config.COMMON.development.enableTDev.get() && (ObjectRenderEditor.get() != null && ObjectRenderEditor.get().currElement == 1 && ObjectRenderEditor.get().GetFromElements(1) != null)) {
            matrixStack.translate(ObjectRenderEditor.get().GetFromElements(1).getxMod(), ObjectRenderEditor.get().GetFromElements(1).getyMod(), ObjectRenderEditor.get().GetFromElements(1).getzMod());
        }*/
        matrixStack.translate(-0.14, -0.4200001, 0);
        matrixStack.translate(0.205, 1.48, 0.19);
        if(tileEntityMBE21.getStackInSlot(1).getItem() == ModItems.MODULE.get())
        {
            if(tileEntityMBE21.getStackInSlot(1).getCount() > 0) {
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityMBE21.getStackInSlot(1), ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, renderBuffers);
            }
            if(tileEntityMBE21.getStackInSlot(1).getCount() > 1) {
                matrixStack.translate(0.12, 0, 0);
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityMBE21.getStackInSlot(1), ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, renderBuffers);
            }
            if(tileEntityMBE21.getStackInSlot(1).getCount() > 2) {
                matrixStack.translate(0.12, 0, 0);
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityMBE21.getStackInSlot(1), ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, renderBuffers);
            }
        }

        matrixStack.pop();
    }

    // this should be true for tileentities which render globally (no render bounding box), such as beacons.
    @Override
    public boolean isGlobalRenderer(UpgradeBenchTileEntity tileEntityMBE21)
    {
        return true;
    }
}
