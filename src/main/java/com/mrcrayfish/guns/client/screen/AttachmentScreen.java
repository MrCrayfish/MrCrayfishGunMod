package com.mrcrayfish.guns.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.client.screen.widget.MiniButton;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModList;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class AttachmentScreen extends AbstractContainerScreen<AttachmentContainer>
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("cgm:textures/gui/attachments.png");
    private static final Component CONFIG_TOOLTIP = Component.translatable("cgm.button.config.tooltip");

    private final Inventory playerInventory;
    private final Container weaponInventory;

    private boolean showHelp = true;
    private int windowZoom = 10;
    private int windowX, windowY;
    private float windowRotationX, windowRotationY;
    private boolean mouseGrabbed;
    private int mouseGrabbedButton;
    private int mouseClickedX, mouseClickedY;

    public AttachmentScreen(AttachmentContainer screenContainer, Inventory playerInventory, Component titleIn)
    {
        super(screenContainer, playerInventory, titleIn);
        this.playerInventory = playerInventory;
        this.weaponInventory = screenContainer.getWeaponInventory();
        this.imageHeight = 184;
    }

    @Override
    protected void init()
    {
        super.init();

        List<MiniButton> buttons = this.gatherButtons();
        for(int i = 0; i < buttons.size(); i++)
        {
            MiniButton button = buttons.get(i);
            switch(Config.CLIENT.buttonAlignment.get())
            {
                case LEFT -> {
                    int titleWidth = this.minecraft.font.width(this.title);
                    button.x = this.leftPos + titleWidth + 8 + 3 + i * 13;
                }
                case RIGHT -> {
                    button.x = this.leftPos + this.imageWidth - 7 - 10 - (buttons.size() - 1 - i) * 13;
                }
            }
            button.y = this.topPos + 5;
            this.addRenderableWidget(button);
        }
    }

    private List<MiniButton> gatherButtons()
    {
        List<MiniButton> buttons = new ArrayList<>();
        if(!Config.CLIENT.hideConfigButton.get())
        {
            buttons.add(new MiniButton(0, 0, 192, 0, GUI_TEXTURES, onPress -> {
                this.openConfigScreen();
            }, (button, matrixStack, mouseX, mouseY) -> {
                this.renderTooltip(matrixStack, CONFIG_TOOLTIP, mouseX, mouseY);
            }));
        }
        return buttons;
    }

    @Override
    public void containerTick()
    {
        super.containerTick();
        if(this.minecraft != null && this.minecraft.player != null)
        {
            if(!(this.minecraft.player.getMainHandItem().getItem() instanceof GunItem))
            {
                Minecraft.getInstance().setScreen(null);
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY); //Render tool tips

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 7, startY + 16 + i * 18, 18, 18))
            {
                IAttachment.Type type = IAttachment.Type.values()[i];
                if(!this.menu.getSlot(i).isActive())
                {
                    this.renderComponentTooltip(poseStack, Arrays.asList(Component.translatable("slot.cgm.attachment." + type.getTranslationKey()), Component.translatable("slot.cgm.attachment.not_applicable")), mouseX, mouseY);
                }
                else if(this.weaponInventory.getItem(i).isEmpty())
                {

                    this.renderComponentTooltip(poseStack, Collections.singletonList(Component.translatable("slot.cgm.attachment." + type.getTranslationKey())), mouseX, mouseY);
                }
            }
        }

        this.children().forEach(widget ->
        {
            if(widget instanceof Button button && button.isHoveredOrFocused())
            {
                button.renderToolTip(poseStack, mouseX, mouseY);
            }
        });
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
    {
        Minecraft minecraft = Minecraft.getInstance();
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(poseStack, this.playerInventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY + 19, 4210752);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        RenderUtil.scissor(left + 26, top + 17, 142, 70);

        PoseStack stack = RenderSystem.getModelViewStack();
        stack.pushPose();
        {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            stack.translate(96, 50, 100);
            stack.translate(this.windowX + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseX - this.mouseClickedX : 0), 0, 0);
            stack.translate(0, this.windowY + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseY - this.mouseClickedY : 0), 0);
            stack.mulPose(Vector3f.XP.rotationDegrees(-30F));
            stack.mulPose(Vector3f.XP.rotationDegrees(this.windowRotationY - (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseY - this.mouseClickedY : 0)));
            stack.mulPose(Vector3f.YP.rotationDegrees(this.windowRotationX + (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseX - this.mouseClickedX : 0)));
            stack.mulPose(Vector3f.YP.rotationDegrees(150F));
            stack.scale(this.windowZoom / 10F, this.windowZoom / 10F, this.windowZoom / 10F);
            stack.scale(90F, -90F, 90F);
            stack.mulPose(Vector3f.XP.rotationDegrees(5F));
            stack.mulPose(Vector3f.YP.rotationDegrees(90F));
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = this.minecraft.renderBuffers().bufferSource();
            GunRenderingHandler.get().renderWeapon(this.minecraft.player, this.minecraft.player.getMainHandItem(), ItemTransforms.TransformType.GROUND, new PoseStack(), buffer, 15728880, 0F);
            buffer.endBatch();
        }
        stack.popPose();
        RenderSystem.applyModelViewMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if(this.showHelp)
        {
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            minecraft.font.draw(poseStack, I18n.get("container.cgm.attachments.window_help"), 56, 38, 0xFFFFFF);
            poseStack.popPose();
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURES);
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, left, top, 0, 0, this.imageWidth, this.imageHeight);

        /* Draws the icons for each attachment slot. If not applicable
         * for the weapon, it will draw a cross instead. */
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            if(!this.menu.getSlot(i).isActive())
            {
                this.blit(poseStack, left + 8, top + 17 + i * 18, 176, 0, 16, 16);
            }
            else if(this.weaponInventory.getItem(i).isEmpty())
            {
                this.blit(poseStack, left + 8, top + 17 + i * 18, 176, 16 + i * 16, 16, 16);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
    {
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
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
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

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

    private void openConfigScreen()
    {
        ModList.get().getModContainerById(Reference.MOD_ID).ifPresent(container ->
        {
            Screen screen = container.getCustomExtension(ConfigGuiHandler.ConfigGuiFactory.class).map(function -> function.screenFunction().apply(this.minecraft, null)).orElse(null);
            if(screen != null)
            {
                this.minecraft.setScreen(screen);
            }
            else if(this.minecraft != null && this.minecraft.player != null)
            {
                MutableComponent modName = Component.literal("Configured");
                modName.setStyle(modName.getStyle()
                        .withColor(ChatFormatting.YELLOW)
                        .withUnderlined(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("cgm.chat.open_curseforge_page")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/configured")));
                Component message = Component.translatable("cgm.chat.install_configured", modName);
                this.minecraft.player.displayClientMessage(message, false);
            }
        });
    }
}
