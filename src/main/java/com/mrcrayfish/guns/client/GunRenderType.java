package com.mrcrayfish.guns.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.client.render.ScreenTextureState;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

/**
 * Author: MrCrayfish
 */

public final class GunRenderType extends RenderType
{
    private static final RenderType BULLET_TRAIL = RenderType.create(Reference.MOD_ID + ":projectile_trail", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER).setCullState(NO_CULL).setTransparencyState(TRANSLUCENT_TRANSPARENCY).createCompositeState(false));
    private static final RenderType SCREEN = RenderType.create(Reference.MOD_ID + ":screen_texture", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.NEW_ENTITY_SHADER).setTexturingState(ScreenTextureState.instance()).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false));
    private static final RenderType MUZZLE_FLASH = RenderType.create(Reference.MOD_ID + ":muzzle_flash", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new RenderStateShard.TextureStateShard(GunRenderingHandler.MUZZLE_FLASH_TEXTURE, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).createCompositeState(true));

    private GunRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
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
