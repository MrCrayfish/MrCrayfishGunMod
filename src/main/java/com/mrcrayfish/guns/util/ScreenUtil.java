package com.mrcrayfish.guns.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

/**
 * Author: MrCrayfish
 */
public class ScreenUtil
{
    /**
     * Creates a scissor test using minecraft screen coordinates instead of pixel coordinates.
     * @param screenX
     * @param screenY
     * @param boxWidth
     * @param boxHeight
     */
    public static void startScissor(int screenX, int screenY, int boxWidth, int boxHeight)
    {
        Minecraft mc = Minecraft.getInstance();
        int scale = (int) mc.getWindow().getGuiScale();
        int x = screenX * scale;
        int y = mc.getWindow().getHeight() - screenY * scale - boxHeight * scale;
        int width = Math.max(0, boxWidth * scale);
        int height = Math.max(0, boxHeight * scale);
        RenderSystem.enableScissor(x, y, width, height);
    }

    public static void endScissor()
    {
        RenderSystem.disableScissor();
    }

    public static boolean isMouseWithin(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
