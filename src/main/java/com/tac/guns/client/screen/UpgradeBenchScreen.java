package com.tac.guns.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.command.GuiEditor;
import com.tac.guns.client.handler.command.ObjectRenderEditor;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.container.UpgradeBenchContainer;
import com.tac.guns.crafting.WorkbenchRecipe;
import com.tac.guns.crafting.WorkbenchRecipes;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.*;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageCraft;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import com.tac.guns.tileentity.WorkbenchTileEntity;
import com.tac.guns.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class UpgradeBenchScreen extends ContainerScreen<UpgradeBenchContainer>
{
    private static final ResourceLocation GUI_BASE = new ResourceLocation("tac:textures/gui/upgrade_table.png");
    private static boolean showRemaining = false;

    /*private Tab currentTab;
    private List<Tab> tabs = new ArrayList<>();*/
    private List<MaterialItem> materials;
    private List<MaterialItem> filteredMaterials;
    private PlayerInventory playerInventory;
    private UpgradeBenchTileEntity workbench;
    private Button btnCraft;
    private CheckBox checkBoxMaterials;
    private ItemStack displayStack;

    public UpgradeBenchScreen(UpgradeBenchContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.workbench = container.getBench();
        this.xSize = 256;
        this.ySize = 184;
        this.materials = new ArrayList<>();
        //this.createTabs(WorkbenchRecipes.getAll(playerInventory.player.world));
        /*if(!this.tabs.isEmpty())
        {
            this.ySize += 28;
        }*/
    }
    @Override
    public void init()
    {
        super.init();
        GuiEditor.GUI_Element data = new GuiEditor.GUI_Element(0,0,0,0);
        if(GuiEditor.get() != null)
        {
            if(GuiEditor.get().currElement == 1 && GuiEditor.get().GetFromElements(GuiEditor.get().currElement) != null)
                data = GuiEditor.get().GetFromElements(GuiEditor.get().currElement);
        }
        this.addButton(new Button(this.guiLeft + 9 + data.getxMod(), this.guiTop + 18 + data.getyMod(), 15+data.getSizeXMod(), 20+data.getSizeYMod(), new StringTextComponent("<"), button ->
        {
            // Apply module to held item
        }));
        this.addButton(new Button(this.guiLeft + 153, this.guiTop + 18, 15, 20, new StringTextComponent(">"), button ->
        {
            // Apply module to held item
        }));

    }

    @Override
    public void tick() // Can the player apply the current module? check recipe along with progression on a weapon
    {
        super.tick();

        for(MaterialItem material : this.materials)
        {
            material.update();
        }

        boolean canCraft = true;
        for(MaterialItem material : this.materials)
        {
            if(!material.isEnabled())
            {
                canCraft = false;
                break;
            }
        }
        this.init();
    }
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        UpgradeBenchScreen.showRemaining = this.checkBoxMaterials.isToggled();

        for(int i = 0; i < this.tabs.size(); i++)
        {
            if(RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, this.guiLeft + 28 * i, this.guiTop - 28, 28, 28))
            {
                this.currentTab = this.tabs.get(i);
                this.loadItem(this.currentTab.getCurrentIndex());
                this.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }

        return result;
    }*/

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        int startX = this.guiLeft;
        int startY = this.guiTop;

        /*for(int i = 0; i < this.tabs.size(); i++)
        {
            if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 28 * i, startY - 28, 28, 28))
            {
                this.renderTooltip(matrixStack, new TranslationTextComponent(this.tabs.get(i).getTabKey()), mouseX, mouseY);
                return;
            }
        }*/

        if (this.filteredMaterials == null)
            return;
        for(int i = 0; i < this.filteredMaterials.size(); i++)
        {
            int itemX = startX + 172;
            int itemY = startY + i * 19 + 63;
            if(RenderUtil.isMouseWithin(mouseX, mouseY, itemX, itemY, 80, 19))
            {
                MaterialItem materialItem = this.filteredMaterials.get(i);
                if(!materialItem.getStack().isEmpty())
                {
                    this.renderTooltip(matrixStack, materialItem.getStack(), mouseX, mouseY);
                    return;
                }
            }
        }

        /*if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 8, startY + 38, 160, 48))
        {
            this.renderTooltip(matrixStack, this.displayStack, mouseX, mouseY);
        }*/
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        //int offset = this.tabs.isEmpty() ? 0 : 28;
        //this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY - 28 + offset, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        /* Fixes partial ticks to use percentage from 0 to 1 */
        partialTicks = Minecraft.getInstance().getRenderPartialTicks();

        int startX = this.guiLeft;
        int startY = this.guiTop;

        RenderSystem.enableBlend();

        GuiEditor.GUI_Element data = new GuiEditor.GUI_Element(0,0,0,0);
        if(GuiEditor.get() != null)
        {
            if(GuiEditor.get().currElement == 2 && GuiEditor.get().GetFromElements(GuiEditor.get().currElement) != null)
                data = GuiEditor.get().GetFromElements(GuiEditor.get().currElement);
        }
        matrixStack.push();
        matrixStack.scale(4f, 4f, 0); //3.87
        this.minecraft.getTextureManager().bindTexture(GUI_BASE);
        this.blit(matrixStack, startX+data.getxMod()-112, startY+data.getyMod()-30, 0, 0, 496, 175);

        matrixStack.pop();

        ItemStack currentItem = this.workbench.getStackInSlot(0);//this.displayStack;
        if(currentItem == null)
            return;
        /*StringBuilder builder = new StringBuilder(currentItem.getDisplayName().getString());
        if(currentItem.getCount() > 1)
        {
            builder.append(TextFormatting.GOLD);
            builder.append(TextFormatting.BOLD);
            builder.append(" x ");
            builder.append(currentItem.getCount());
        }
        this.drawCenteredString(matrixStack, this.font, builder.toString(), startX + 88, startY + 22, Color.WHITE.getRGB());*/
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(startX + 8, startY + 17, 160, 70);

        IRenderTypeBuffer.Impl buffer = this.minecraft.getRenderTypeBuffers().getBufferSource();
        RenderSystem.pushMatrix();
        {
            RenderSystem.translatef(startX + 88, startY + 60, 100);
            RenderSystem.scalef(50F, -50F, 50F);
            RenderSystem.rotatef(5F, 1, 0, 0);
            RenderSystem.rotatef(Minecraft.getInstance().player.ticksExisted + partialTicks, 0, 1, 0);

            RenderSystem.enableRescaleNormal();
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            buffer = this.minecraft.getRenderTypeBuffers().getBufferSource();
            ObjectRenderEditor.RENDER_Element dataW = new ObjectRenderEditor.RENDER_Element(0,0,0,0);
            if(ObjectRenderEditor.get() != null)
            {
                if(ObjectRenderEditor.get().currElement == 3 && ObjectRenderEditor.get().GetFromElements(GuiEditor.get().currElement) != null)
                    dataW = ObjectRenderEditor.get().GetFromElements(ObjectRenderEditor.get().currElement);
            }
            matrixStack.translate(dataW.getxMod(), dataW.getyMod(), 0);
            GunRenderingHandler.get().renderWeapon(Minecraft.getInstance().player, currentItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
            //Minecraft.getInstance().getItemRenderer().renderItem(currentItem, ItemCameraTransforms.TransformType.FIXED, false, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, RenderUtil.getModel(currentItem));

            buffer.finish();

            RenderSystem.disableAlphaTest();
            RenderSystem.disableRescaleNormal();
        }
        RenderSystem.popMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        this.filteredMaterials = this.getMaterials();
        for(int i = 0; i < this.filteredMaterials.size(); i++)
        {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.minecraft.getTextureManager().bindTexture(GUI_BASE);

            MaterialItem materialItem = this.filteredMaterials.get(i);
            ItemStack stack = materialItem.stack;
            if(!stack.isEmpty())
            {
                RenderHelper.disableStandardItemLighting();
                if(materialItem.isEnabled())
                {
                    this.blit(matrixStack, startX + 172, startY + i * 19 + 63, 0, 184, 80, 19);
                }
                else
                {
                    this.blit(matrixStack, startX + 172, startY + i * 19 + 63, 0, 222, 80, 19);
                }

                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                String name = stack.getDisplayName().getString();
                if(this.font.getStringWidth(name) > 55)
                {
                    name = this.font.func_238412_a_(name, 50).trim() + "...";
                }
                this.font.drawString(matrixStack, name, startX + 172 + 22, startY + i * 19 + 6 + 63, Color.WHITE.getRGB());

                Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, startX + 172 + 2, startY + i * 19 + 1 + 63);

                if(this.checkBoxMaterials.isToggled())
                {
                    int count = InventoryUtil.getItemStackAmount(Minecraft.getInstance().player, stack);
                    stack = stack.copy();
                    stack.setCount(stack.getCount() - count);
                }

                //GunRenderingHandler.get().renderWeapon(minecraft.player, stack, ItemCameraTransforms.TransformType.GROUND, matrixStack, buffer, )
                Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(this.font, stack, startX + 172 + 2, startY + i * 19 + 1 + 63, null);
                //GunRenderingHandler.get().renderWeapon(this.minecraft.player, this.minecraft.player.getHeldItemMainhand(), ItemCameraTransforms.TransformType.GROUND, matrixStack, buffer, 15728880, 0F); // GROUND, matrixStack, buffer, 15728880, 0F);
            }
        }
    }

    private List<MaterialItem> getMaterials()
    {
        List<MaterialItem> materials = NonNullList.withSize(6, new MaterialItem(ItemStack.EMPTY));
        List<MaterialItem> filteredMaterials = this.materials.stream().filter(materialItem -> this.checkBoxMaterials.isToggled() ? !materialItem.isEnabled() : !materialItem.stack.isEmpty()).collect(Collectors.toList());
        for(int i = 0; i < filteredMaterials.size() && i < materials.size(); i++)
        {
            materials.set(i, filteredMaterials.get(i));
        }
        return materials;
    }
    public static class MaterialItem {
        public static final MaterialItem EMPTY = new MaterialItem();

        private boolean enabled = false;
        private ItemStack stack = ItemStack.EMPTY;

        private MaterialItem() {
        }

        private MaterialItem(ItemStack stack) {
            this.stack = stack;
        }

        public ItemStack getStack() {
            return stack;
        }

        public void update() {
            if (!this.stack.isEmpty()) {
                this.enabled = InventoryUtil.hasItemStack(Minecraft.getInstance().player, this.stack);
            }
        }

        public boolean isEnabled() {
            return this.stack.isEmpty() || this.enabled;
        }
    }
}
