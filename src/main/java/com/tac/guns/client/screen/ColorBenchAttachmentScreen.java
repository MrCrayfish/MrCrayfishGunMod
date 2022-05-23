package com.tac.guns.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.container.ColorBenchContainer;
import com.tac.guns.crafting.WorkbenchRecipe;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.IColored;
import com.tac.guns.item.IWeaponColorable;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ColorBenchAttachmentScreen extends ContainerScreen<ColorBenchContainer>
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("tac:textures/gui/painter.png");

    private final PlayerInventory playerInventory;

    // Color bench data
    private int colorTableSize = 8; // -1 in actual use (0-7 = 8) slots in total
    private boolean grayScaleActive = false; // set to true if the weapon has a loaded gray scale skin ready

    private boolean showHelp = false; // Default true

    private Button btnApply;

    // Weapons display
    private int windowZoom = 10;
    private int windowX, windowY;
    private float windowRotationX, windowRotationY;
    private boolean mouseGrabbed;
    private int mouseGrabbedButton;
    private int mouseClickedX, mouseClickedY;

    public ColorBenchAttachmentScreen(ColorBenchContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn)
    {
        super(screenContainer, playerInventory, titleIn);
        this.playerInventory = playerInventory;
        this.ySize = 184;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.minecraft != null && this.minecraft.player != null)
        {
            if(!(this.minecraft.player.getHeldItemMainhand().getItem() instanceof GunItem || this.minecraft.player.getHeldItemMainhand().getItem() instanceof ScopeItem))
            {
                Minecraft.getInstance().displayGuiScreen(null);
            }
        }
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY); //Render tool tips
        //matrixStack.translate(-44,0,0);
        //Matrixstack matrixExtra = matrixStack.scale(1.2f,1.2f,1.2f);
        //matrixStack.scale(1.5f,1.5f,1.5f);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        //matrixStack.translate(44,0,0);

        /*int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        for(int i = 0; i < colorTableSize; i++)
        {
            if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 7+ i * 18, startY + 16 , 18, 18))
            {
                if(!this.container.getSlot(i).isEnabled())
                {
                    this.func_243308_b(matrixStack, Arrays.asList(new TranslationTextComponent("slot.tac.attachment." + IWeaponColorable.WeaponColorSegment.byTagKey()), new TranslationTextComponent("slot.tac.attachment.not_applicable")), mouseX, mouseY);
                }
                else if(this.weaponInventory.getStackInSlot(i).isEmpty())
                {

                    this.func_243308_b(matrixStack, Collections.singletonList(new TranslationTextComponent("slot.tac.attachment." + type.getTranslationKey())), mouseX, mouseY);
                }
            }
        }*/
    }

    /*@Override
    public void init()
    {
        super.init();

        this.btnApply = this.addButton(new Button(this.guiLeft + 195, this.guiTop + 16, 74, 20, new TranslationTextComponent("gui.tac.workbench.assemble"), button ->
        {
            PacketHandler.getPlayChannel().sendToServer(new MessageCraft(registryName, this.workbench.getPos()));
        }));
        this.btnApply.active = false;
    }

    @Override
    public void tick()
    {
        super.tick();

        for(WorkbenchScreen.MaterialItem material : this.materials)
        {
            material.update();
        }

        boolean canCraft = true;
        for(WorkbenchScreen.MaterialItem material : this.materials)
        {
            if(!material.isEnabled())
            {
                canCraft = false;
                break;
            }
        }

        this.btnApply.active = canCraft;
        this.updateColor();
    }

    private void updateColor()
    {
        if(this.currentTab != null)
        {
            ItemStack item = this.displayStack;
            if(item.getItem() instanceof IColored && ((IColored) item.getItem()).canColor(item))
            {
                IColored colored = (IColored) item.getItem();
                if(!this.workbench.getStackInSlot(0).isEmpty())
                {
                    ItemStack dyeStack = this.workbench.getStackInSlot(0);
                    if(dyeStack.getItem() instanceof DyeItem)
                    {
                        DyeColor color = ((DyeItem) dyeStack.getItem()).getDyeColor();
                        float[] components = color.getColorComponentValues();
                        int red = (int) (components[0] * 255F);
                        int green = (int) (components[1] * 255F);
                        int blue = (int) (components[2] * 255F);
                        colored.setColor(item, ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF)));
                    }
                    else
                    {
                        colored.removeColor(item);
                    }
                }
                else
                {
                    colored.removeColor(item);
                }
            }
        }
    }*/

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        Minecraft minecraft = Minecraft.getInstance();
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX+30, (float)this.titleY, 4210752);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int left = (this.width - this.xSize) / 2;
        int top = (this.height - this.ySize) / 2;
        RenderUtil.scissor(left + 26, top + 17, 272, 286);
        //RenderSystem.scalef(1.5f,1.5f,1.5f);

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

            IRenderTypeBuffer.Impl buffer = this.minecraft.getRenderTypeBuffers().getBufferSource();
            //GunRenderingHandler.get().renderWeapon(this.minecraft.player, this.minecraft.player.getHeldItemMainhand(), ItemCameraTransforms.TransformType.GROUND, matrixStack, buffer, 15728880, 0F);
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
            minecraft.fontRenderer.drawString(matrixStack, I18n.format("container.tac.attachments.window_help"), 56, 38, 0xFFFFFF);
            RenderSystem.popMatrix();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        int left = (this.width - this.xSize)/ 2;
        int top = (this.height - this.ySize)/ 2;

        matrixStack.push();
        matrixStack.scale(2,2,2);
        matrixStack.translate(-100, -29f,0);//-27.5f,0);
        this.blit(matrixStack, left, top, 0, 0, this.xSize, this.ySize);
        matrixStack.pop();

        /* Draws the icons for each attachment slot. If not applicable
         * for the weapon, it will draw a cross instead. */
        for(int i = 0; i < IWeaponColorable.WeaponColorSegment.values().length; i++)
        {
            if(!this.container.getSlot(i).isEnabled())
            {
                this.blit(matrixStack, left + 8, top + 17 + i * 18, 176, 0, 16, 16);
            }
            else
            {
                this.blit(matrixStack, left + 8, top + 17 + i * 18, 176, 16 + i * 16, 16, 16);
            }
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
