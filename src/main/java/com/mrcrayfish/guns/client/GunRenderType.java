package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.client.render.ScreenTextureState;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Author: MrCrayfish
 */
public final class GunRenderType extends RenderType
{
    private static final RenderType BULLET_TRAIL = RenderType.create(Reference.MOD_ID + ":projectile_trail", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL_QUADS, 256, true, true, RenderType.State.builder().setCullState(NO_CULL).setAlphaState(DEFAULT_ALPHA).setTransparencyState(TRANSLUCENT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType SCREEN = RenderType.create(Reference.MOD_ID + ":screen_texture", DefaultVertexFormats.NEW_ENTITY, GL_QUADS, 256, true, false, RenderType.State.builder().setTexturingState(ScreenTextureState.instance()).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false));
    private static final RenderType MUZZLE_FLASH = RenderType.create(Reference.MOD_ID + ":muzzle_flash", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL_QUADS, 256, true, false, RenderType.State.builder().setTextureState(new RenderState.TextureState(GunRenderingHandler.MUZZLE_FLASH_TEXTURE, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setAlphaState(DEFAULT_ALPHA).setCullState(NO_CULL).createCompositeState(true));

    private GunRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
    {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getBulletTrail()
    {
        return BULLET_TRAIL;
    }

    public static RenderType getScreen()
    {
        return SCREEN;
    }

    public static RenderType getMuzzleFlash()
    {
        return MUZZLE_FLASH;
    }
}
