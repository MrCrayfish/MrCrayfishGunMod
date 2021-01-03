package com.mrcrayfish.guns.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

/**
 * A texture state that represents the buffer after the world has been rendered but before the HUD
 * is rendered. Used for rendering scopes. This object is restricted to one instance.
 *
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ScreenTextureState extends RenderState.TexturingState
{
    private static ScreenTextureState instance = null;

    public static ScreenTextureState instance()
    {
        if(instance == null)
        {
            instance = new ScreenTextureState();
        }
        return instance;
    }

    private int textureId = -1;

    private ScreenTextureState()
    {
        super("screen_texture", () -> {
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableTexture();
            RenderSystem.bindTexture(ScreenTextureState.instance().getTextureId());
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }, () -> {
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        });
    }

    public int getTextureId()
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        if(this.textureId == -1)
        {
            this.textureId = TextureUtil.generateTextureId();
        }
        return this.textureId;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderLastWorld(RenderWorldLastEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.bindTexture(instance().getTextureId());
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight(), 0);
    }
}
