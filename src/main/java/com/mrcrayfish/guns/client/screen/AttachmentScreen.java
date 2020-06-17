package com.mrcrayfish.guns.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.Collections;

/**
 * Author: MrCrayfish
 */
public class AttachmentScreen extends ContainerScreen<AttachmentContainer>
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("cgm:textures/gui/attachments.png");

    private final PlayerInventory playerInventory;
    private final IInventory weaponInventory;

    private boolean showHelp = true;
    private int windowZoom = 10;
    private int windowX, windowY;
    private float windowRotationX, windowRotationY;
    private boolean mouseGrabbed;
    private int mouseGrabbedButton;
    private int mouseClickedX, mouseClickedY;

    public AttachmentScreen(AttachmentContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn)
    {
        super(screenContainer, playerInventory, titleIn);
        this.playerInventory = playerInventory;
        this.weaponInventory = screenContainer.getWeaponInventory();
        this.ySize = 184;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.minecraft != null && this.minecraft.player != null)
        {
            if(!(this.minecraft.player.getHeldItemMainhand().getItem() instanceof GunItem))
            {
                Minecraft.getInstance().displayGuiScreen(null);
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 7, startY + 16, 18, 18))
        {
            if(!this.container.getSlot(0).isEnabled())
            {
                this.renderTooltip(Arrays.asList(I18n.format("slot.cgm.attachment.scope"), I18n.format("slot.cgm.attachment.not_applicable")), mouseX, mouseY, this.minecraft.fontRenderer);
            }
            else if(this.weaponInventory.getStackInSlot(0).isEmpty())
            {
                this.renderTooltip(Collections.singletonList(I18n.format("slot.cgm.attachment.scope")), mouseX, mouseY, this.minecraft.fontRenderer);
            }
        }

        if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 7, startY + 34, 18, 18))
        {
            if(!this.container.getSlot(1).isEnabled())
            {
                this.renderTooltip(Arrays.asList(I18n.format("slot.cgm.attachment.barrel"), I18n.format("slot.cgm.attachment.not_applicable")), mouseX, mouseY, this.minecraft.fontRenderer);
            }
            else if(this.weaponInventory.getStackInSlot(1).isEmpty())
            {
                this.renderTooltip(Collections.singletonList(I18n.format("slot.cgm.attachment.barrel")), mouseX, mouseY, this.minecraft.fontRenderer);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.fontRenderer.drawString(this.title.getFormattedText(), 8, 6, 4210752);
        minecraft.fontRenderer.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int left = (this.width - this.xSize) / 2;
        int top = (this.height - this.ySize) / 2;
        RenderUtil.scissor(left + 26, top + 17, 142, 70);

        RenderSystem.pushMatrix();
        {
            RenderSystem.translatef(96, 50, 100);
            RenderSystem.translated(this.windowX + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseX - this.mouseClickedX : 0), 0, 0);
            RenderSystem.translated(0, this.windowY + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseY - this.mouseClickedY : 0), 0);
            RenderSystem.rotatef(-30F, 1, 0, 0);
            RenderSystem.rotatef(this.windowRotationY - (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseY - this.mouseClickedY : 0), 1, 0, 0);
            RenderSystem.rotatef(this.windowRotationX + (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseX - this.mouseClickedX : 0), 0, 1, 0);
            RenderSystem.rotatef(150F, 0, 1, 0);
            RenderSystem.scalef(this.windowZoom / 10F, this.windowZoom / 10F, this.windowZoom / 10F);
            RenderSystem.scalef(90F, -90F, 90F);
            RenderSystem.rotatef(5F, 1, 0, 0);
            RenderSystem.rotatef(90F, 0, 1, 0);

            RenderSystem.enableRescaleNormal();
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            MatrixStack matrixStack = new MatrixStack();
            IRenderTypeBuffer.Impl buffer = this.minecraft.getRenderTypeBuffers().getBufferSource();
            ClientHandler.getGunRenderer().renderWeapon(this.minecraft.player, this.minecraft.player.getHeldItemMainhand(), ItemCameraTransforms.TransformType.GROUND, matrixStack, buffer, 15728880, 0F);
            buffer.finish();

            RenderSystem.disableAlphaTest();
            RenderSystem.disableRescaleNormal();
        }
        RenderSystem.popMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if(this.showHelp)
        {
            RenderSystem.pushMatrix();
            RenderSystem.scalef(0.5F, 0.5F, 0.5F);
            minecraft.fontRenderer.drawString(I18n.format("container.cgm.attachments.window_help"), 56, 38, 0xFFFFFF);
            RenderSystem.popMatrix();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        int left = (this.width - this.xSize) / 2;
        int top = (this.height - this.ySize) / 2;
        this.blit(left, top, 0, 0, this.xSize, this.ySize);

        if(!this.container.getSlot(0).isEnabled())
        {
            this.blit(left + 8, top + 17, 176, 32, 16, 16);
        }
        else if(this.weaponInventory.getStackInSlot(0).isEmpty())
        {
            this.blit(left + 8, top + 17, 176, 0, 16, 16);
        }

        if(!this.container.getSlot(1).isEnabled())
        {
            this.blit(left + 8, top + 35, 176, 32, 16, 16);
        }
        else if(this.weaponInventory.getStackInSlot(1).isEmpty())
        {
            this.blit(left + 8, top + 35, 176, 16, 16, 16);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
    {
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        if(RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, startX + 26, startY + 17, 142, 70))
        {
            if(scroll < 0 && this.windowZoom > 0)
            {
                this.showHelp = false;
                this.windowZoom--;
            }
            else if(scroll > 0)
            {
                this.showHelp = false;
                this.windowZoom++;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        if(RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, startX + 26, startY + 17, 142, 70))
        {
            if(!this.mouseGrabbed && (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT))
            {
                this.mouseGrabbed = true;
                this.mouseGrabbedButton = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? 1 : 0;
                this.mouseClickedX = (int) mouseX;
                this.mouseClickedY = (int) mouseY;
                this.showHelp = false;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if(this.mouseGrabbed)
        {
            if(this.mouseGrabbedButton == 0 && button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
            {
                this.mouseGrabbed = false;
                this.windowX += (mouseX - this.mouseClickedX - 1);
                this.windowY += (mouseY - this.mouseClickedY);
            }
            else if(mouseGrabbedButton == 1 && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
            {
                this.mouseGrabbed = false;
                this.windowRotationX += (mouseX - this.mouseClickedX);
                this.windowRotationY -= (mouseY - this.mouseClickedY);
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
