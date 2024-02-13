package com.mrcrayfish.guns.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.client.screen.widget.MiniButton;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.common.container.slot.AttachmentSlot;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class AttachmentScreen extends AbstractContainerScreen<AttachmentContainer> {
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

    public AttachmentScreen(AttachmentContainer screenContainer, Inventory playerInventory, Component titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.playerInventory = playerInventory;
        this.weaponInventory = screenContainer.getWeaponInventory();
        this.imageHeight = 184;
    }

    @Override
    protected void init() {
        super.init();

        List<MiniButton> buttons = this.gatherButtons();
        for (int i = 0; i < buttons.size(); i++) {
            MiniButton button = buttons.get(i);
            switch (Config.CLIENT.buttonAlignment.get()) {
                case LEFT -> {
                    int titleWidth = this.minecraft.font.width(this.title);
                    button.setX(this.leftPos + titleWidth + 8 + 3 + i * 13);
                }
                case RIGHT -> {
                    button.setX(this.leftPos + this.imageWidth - 7 - 10 - (buttons.size() - 1 - i) * 13);
                }
            }
            button.setY(this.topPos + 5);
            this.addRenderableWidget(button);
        }
    }

    private List<MiniButton> gatherButtons() {
        List<MiniButton> buttons = new ArrayList<>();
        if (!Config.CLIENT.hideConfigButton.get()) {
            MiniButton configButton = new MiniButton(0, 0, 192, 0, GUI_TEXTURES, onPress -> this.openConfigScreen());
            configButton.setTooltip(Tooltip.create(CONFIG_TOOLTIP));
            buttons.add(configButton);
        }
        return buttons;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.minecraft != null && this.minecraft.player != null) {
            if (!(this.minecraft.player.getMainHandItem().getItem() instanceof GunItem)) {
                Minecraft.getInstance().setScreen(null);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY); //Render tool tips

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        for (int i = 0; i < IAttachment.Type.values().length; i++) {
            if (RenderUtil.isMouseWithin(mouseX, mouseY, startX + 7, startY + 16 + i * 18, 18, 18)) {
                IAttachment.Type type = IAttachment.Type.values()[i];
                if (!this.menu.getSlot(i).isActive()) {
                    graphics.renderComponentTooltip(this.font, Arrays.asList(Component.translatable("slot.cgm.attachment." + type.getTranslationKey()), Component.translatable("slot.cgm.attachment.not_applicable")), mouseX, mouseY);
                } else if (this.menu.getSlot(i) instanceof AttachmentSlot slot && slot.getItem().isEmpty() && !this.isCompatible(this.menu.getCarried(), slot)) {
                    graphics.renderComponentTooltip(this.font, Arrays.asList(Component.translatable("slot.cgm.attachment.incompatible").withStyle(ChatFormatting.YELLOW)), mouseX, mouseY);
                } else if (this.weaponInventory.getItem(i).isEmpty()) {
                    graphics.renderComponentTooltip(this.font, Collections.singletonList(Component.translatable("slot.cgm.attachment." + type.getTranslationKey())), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventory.getDisplayName(), this.inventoryLabelX, this.inventoryLabelY + 19, 4210752, false);

        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        graphics.enableScissor(left + 26, top + 17, left + 26 + 142, top + 17 + 70);
        graphics.pose().pushPose();
        graphics.pose().translate(96, 50, 150);
        graphics.pose().translate(this.windowX + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseX - this.mouseClickedX : 0), 0, 0);
        graphics.pose().translate(0, this.windowY + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseY - this.mouseClickedY : 0), 0);
        graphics.pose().mulPose(Axis.XP.rotationDegrees(-30F));
        graphics.pose().mulPose(Axis.XP.rotationDegrees(this.windowRotationY - (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseY - this.mouseClickedY : 0)));
        graphics.pose().mulPose(Axis.YP.rotationDegrees(this.windowRotationX + (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseX - this.mouseClickedX : 0)));
        graphics.pose().mulPose(Axis.YP.rotationDegrees(150F));
        graphics.pose().scale(this.windowZoom / 10F, this.windowZoom / 10F, this.windowZoom / 10F);
        graphics.pose().mulPose(Axis.YP.rotationDegrees(90F));
        graphics.pose().mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        graphics.pose().scale(90.0F, 90.0F, 90.0F);
        PoseStack modelStack = RenderSystem.getModelViewStack();
        modelStack.pushPose();
        modelStack.mulPoseMatrix(graphics.pose().last().pose());
        RenderSystem.applyModelViewMatrix();
        MultiBufferSource.BufferSource buffer = this.minecraft.renderBuffers().bufferSource();
        GunRenderingHandler.get().renderWeapon(this.minecraft.player, this.minecraft.player.getMainHandItem(), ItemDisplayContext.GROUND, new PoseStack(), buffer, 15728880, 0F);
        buffer.endBatch();
        graphics.pose().popPose();
        modelStack.popPose();
        RenderSystem.applyModelViewMatrix();
        graphics.disableScissor();

        if (this.showHelp) {
            graphics.pose().pushPose();
            graphics.pose().scale(0.5F, 0.5F, 0.5F);
            graphics.drawString(minecraft.font, I18n.get("container.cgm.attachments.window_help"), 56, 38, 0xFFFFFF, false);
            graphics.pose().popPose();
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI_TEXTURES, left, top, 0, 0, this.imageWidth, this.imageHeight);

        /* Draws the icons for each attachment slot. If not applicable
         * for the weapon, it will draw a cross instead. */
        for (int i = 0; i < IAttachment.Type.values().length; i++) {
            if (!this.canPlaceAttachmentInSlot(this.menu.getCarried(), this.menu.getSlot(i))) {
                graphics.blit(GUI_TEXTURES, left + 8, top + 17 + i * 18, 176, 0, 16, 16);
            } else if (this.weaponInventory.getItem(i).isEmpty()) {
                graphics.blit(GUI_TEXTURES, left + 8, top + 17 + i * 18, 176, 16 + i * 16, 16, 16);
            }
        }
    }

    private boolean canPlaceAttachmentInSlot(ItemStack stack, Slot slot) {
        if (!slot.isActive()) return false;

        if (!slot.equals(this.getSlotUnderMouse())) return true;

        if (!slot.getItem().isEmpty()) return true;

        if (!(slot instanceof AttachmentSlot s)) return true;

        if (!(stack.getItem() instanceof IAttachment<?> a)) return true;

        if (!s.getType().equals(a.getType())) return true;

        return s.mayPlace(stack);
    }

    private boolean isCompatible(ItemStack stack, AttachmentSlot slot) {
        if (stack.isEmpty()) return true;

        if (!(stack.getItem() instanceof IAttachment<?> attachment)) return false;

        if (!attachment.getType().equals(slot.getType())) return true;

        if (!attachment.canAttachTo(stack)) return false;

        return slot.mayPlace(stack);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        if (RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, startX + 26, startY + 17, 142, 70)) {
            if (scroll < 0 && this.windowZoom > 0) {
                this.showHelp = false;
                this.windowZoom--;
            } else if (scroll > 0) {
                this.showHelp = false;
                this.windowZoom++;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        if (RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, startX + 26, startY + 17, 142, 70)) {
            if (!this.mouseGrabbed && (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
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
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.mouseGrabbed) {
            if (this.mouseGrabbedButton == 0 && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                this.mouseGrabbed = false;
                this.windowX += (mouseX - this.mouseClickedX - 1);
                this.windowY += (mouseY - this.mouseClickedY);
            } else if (mouseGrabbedButton == 1 && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                this.mouseGrabbed = false;
                this.windowRotationX += (mouseX - this.mouseClickedX);
                this.windowRotationY -= (mouseY - this.mouseClickedY);
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void openConfigScreen() {
        ModList.get().getModContainerById(Reference.MOD_ID).ifPresent(container -> {
            Screen screen = container.getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class).map(function -> function.screenFunction().apply(this.minecraft, null)).orElse(null);
            if (screen != null) {
                this.minecraft.setScreen(screen);
            } else if (this.minecraft != null && this.minecraft.player != null) {
                MutableComponent modName = Component.literal("Configured");
                modName.setStyle(modName.getStyle().withColor(ChatFormatting.YELLOW).withUnderlined(true).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("cgm.chat.open_curseforge_page"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/configured")));
                Component message = Component.translatable("cgm.chat.install_configured", modName);
                this.minecraft.player.displayClientMessage(message, false);
            }
        });
    }
}
