package com.tac.guns.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.Reference;
import com.tac.guns.util.OptifineHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;

import static org.lwjgl.opengl.GL11.*;

/**
 * A texture state that represents the buffer after the world has been rendered but before the HUD
 * is rendered. Used for rendering scopes. This object is restricted to one get.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ScreenTextureState extends RenderState.TexturingState
{
    private static ScreenTextureState instance = null;

    public static ScreenTextureState instance()
    {
        return instance == null ? instance = new ScreenTextureState() : instance;
    }

    private int textureId;
    private int lastWindowWidth;
    private int lastWindowHeight;

    private ScreenTextureState()
    {
        super("screen_texture", () ->
        {
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableTexture();
            RenderSystem.bindTexture(instance().getTextureId());
        }, () ->
        {
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        });
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onRenderWorldLast);
    }

    private int getTextureId()
    {
        if(this.textureId == 0)
        {
            this.textureId = TextureUtil.generateTextureId();
            // Texture params only need to be set once, not once per frame
            RenderSystem.bindTexture(this.textureId);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        }
        return this.textureId;
    }

    private void onRenderWorldLast(RenderWorldLastEvent event)
    {
/*        // Yep scopes will never work with shaders
        if(OptifineHelper.isShadersEnabled())
            return;*/

        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();

        // OpenGL will spit out an error (GL_INVALID_VALUE) if the window is minimised (or draw calls stop)
        // It seems just testing the width or height if it's zero is enough to prevent it
        if(mainWindow.getWidth() <= 0 || mainWindow.getHeight() <= 0)
            return;

        RenderSystem.bindTexture(this.getTextureId());
        if(mainWindow.getWidth() != this.lastWindowWidth || mainWindow.getHeight() != this.lastWindowHeight)
        {
            // When window resizes the texture needs to be re-initialized and copied, so both are done in the same call
            this.lastWindowWidth = mainWindow.getWidth();
            this.lastWindowHeight = mainWindow.getHeight();
            glCopyTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 0, 0, mainWindow.getFramebufferWidth(), mainWindow.getFramebufferHeight(), 0);
        }
        else
        {
            // Copy sub-image is faster than copy because the texture does not need to be initialized
            glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, mainWindow.getFramebufferWidth(), mainWindow.getFramebufferHeight());
        }
    }
}
