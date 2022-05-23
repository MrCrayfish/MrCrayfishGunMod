package com.tac.guns.client;

import com.tac.guns.Reference;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.render.ScreenTextureState;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public final class GunRenderType extends RenderType
{
    private static final RenderType BULLET_TRAIL = RenderType.makeType(Reference.MOD_ID + ":projectile_trail", DefaultVertexFormats.POSITION_COLOR, GL_QUADS, 256, true, true, RenderType.State.getBuilder().cull(CULL_DISABLED).alpha(DEFAULT_ALPHA).transparency(TRANSLUCENT_TRANSPARENCY).build(false));
    private static final RenderType SCREEN = RenderType.makeType(Reference.MOD_ID + ":screen_texture", DefaultVertexFormats.ENTITY, GL_QUADS, 1024, true, false, RenderType.State.getBuilder().texturing(ScreenTextureState.instance()).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).cull(RenderState.CULL_ENABLED).build(false)); //256
    private static final RenderType MUZZLE_FLASH = RenderType.makeType(Reference.MOD_ID + ":muzzle_flash", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL_QUADS, 256, true, false, RenderType.State.getBuilder().texture(new RenderState.TextureState(GunRenderingHandler.MUZZLE_FLASH_TEXTURE, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).build(true));
    private static final RenderType MUZZLE_SMOKE = RenderType.makeType(Reference.MOD_ID + ":muzzle_smoke", DefaultVertexFormats.POSITION_COLOR_TEX, GL_QUADS, 256, true, false, RenderType.State.getBuilder().texture(new RenderState.TextureState(GunRenderingHandler.MUZZLE_SMOKE_TEXTURE, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).build(true));

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
    } // E : Figuring out how to refactor this = fixing all of Optifine's scope issues

    public static RenderType getMuzzleSmoke() { return MUZZLE_SMOKE; }

    public static RenderType getMuzzleFlash()
    {
        return MUZZLE_FLASH;
    }
}
