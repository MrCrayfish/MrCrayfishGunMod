package com.mrcrayfish.guns.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: MrCrayfish
 */
@SideOnly(Side.CLIENT)
public class GuiCheckBox extends Gui
{
    private static final ResourceLocation GUI = new ResourceLocation("cgm:textures/gui/components.png");

    private boolean toggled = false;
    private int left, top;
    private String title;

    public GuiCheckBox(int left, int top, String title)
    {
        this.left = left;
        this.top = top;
        this.title = title;
    }

    public void setToggled(boolean toggled)
    {
        this.toggled = toggled;
    }

    public boolean isToggled()
    {
        return toggled;
    }

    public void draw(Minecraft mc, int guiLeft, int guiTop)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI);
        this.drawTexturedModalRect(guiLeft + left, guiTop + top, 0, 0, 8, 8);
        if(toggled)
        {
            this.drawTexturedModalRect(guiLeft + left, guiTop + top - 1, 8, 0, 9, 8);
        }
        mc.fontRenderer.drawString(title, guiLeft + left + 12, guiTop + top, 16777215);
    }

    public void handleClick(int guiLeft, int guiTop, int mouseX, int mouseY, int mouseButton)
    {
        if(mouseButton != 0)
            return;

        if(mouseX >= guiLeft + left && mouseX < guiLeft + left + 8 && mouseY >= guiTop + top && mouseY < guiTop + top + 8)
        {
            toggled = !toggled;
        }
    }
}
