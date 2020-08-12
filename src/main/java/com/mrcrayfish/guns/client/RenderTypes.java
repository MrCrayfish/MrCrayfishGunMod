package com.mrcrayfish.guns.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * author: mrcrayfish
 */
public class RenderTypes
{
    public static final RenderState.CullState CULL_DISABLED = new RenderState.CullState(false);
    public static final RenderState.AlphaState DEFAULT_ALPHA = new RenderState.AlphaState(0.0F);
    public static final RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = new RenderState.TransparencyState("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }, () -> {
        RenderSystem.disableBlend();
    });

    public static RenderType getBulletTrail()
    {
        return RenderType.makeType("cgm:projectile_trail", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, false, true, RenderType.State.getBuilder().cull(CULL_DISABLED).alpha(DEFAULT_ALPHA).transparency(TRANSLUCENT_TRANSPARENCY).build(false));
    }
}
