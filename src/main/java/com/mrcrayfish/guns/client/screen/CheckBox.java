package com.mrcrayfish.guns.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class CheckBox extends Widget
{
    private static final ResourceLocation GUI = new ResourceLocation("cgm:textures/gui/components.png");

    private boolean toggled = false;

    public CheckBox(int left, int top, String title)
    {
        super(left, top, title);
        this.width = 8;
        this.height = 8;
    }

    public void setToggled(boolean toggled)
    {
        this.toggled = toggled;
    }

    public boolean isToggled()
    {
        return this.toggled;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(GUI);
        this.blit(this.x, this.y, 0, 0, 8, 8);
        if(this.toggled)
        {
            this.blit(this.x, this.y - 1, 8, 0, 9, 8);
        }
        minecraft.fontRenderer.drawString(this.getMessage(), this.x + 12, this.y, 0xFFFFFF);
    }

    @Override
    public void onClick(double mouseX, double mouseY)
    {
        this.toggled = !this.toggled;
    }
}