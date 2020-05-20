package com.mrcrayfish.guns.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.container.WorkbenchContainer;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import com.mrcrayfish.guns.item.ColoredItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageCraft;
import com.mrcrayfish.guns.tileentity.WorkbenchTileEntity;
import com.mrcrayfish.guns.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class WorkbenchScreen extends ContainerScreen<WorkbenchContainer>
{
    private static final int MAX_TRANSITION_TICKS = 5;
    private static final ResourceLocation GUI = new ResourceLocation("cgm:textures/gui/workbench.png");

    private List<MaterialItem> materials;
    private List<MaterialItem> filteredMaterials;
    private static int currentIndex = 0;
    private static int previousIndex = 0;
    private static int oldRecipesSize = 0;
    private static boolean showRemaining = false;
    private NonNullList<WorkbenchRecipe> recipes;
    private PlayerInventory playerInventory;
    private WorkbenchTileEntity workbench;
    private Button btnCraft;
    private CheckBox checkBoxMaterials;
    private boolean transitioning;
    private int transitionProgress = MAX_TRANSITION_TICKS;
    private int prevTransitionProgress = MAX_TRANSITION_TICKS;
    private ItemTransformVec3f displayProperty;
    private ItemTransformVec3f prevDisplayProperty;

    public WorkbenchScreen(WorkbenchContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.workbench = container.getWorkbench();
        this.xSize = 289;
        this.ySize = 202;
        this.materials = new ArrayList<>();
        this.recipes = WorkbenchRecipes.getAll(playerInventory.player.world);
        if(oldRecipesSize != this.recipes.size()) {
            currentIndex = 0;
            previousIndex = 0;
            oldRecipesSize = this.recipes.size();
        }
    }

    @Override
    public void init()
    {
        super.init();
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        this.addButton(new Button(startX, startY, 15, 20, "<", button ->
        {
            if(currentIndex - 1 < 0)
            {
                this.loadItem(this.recipes.size() - 1);
            }
            else
            {
                this.loadItem(currentIndex - 1);
            }
        }));
        this.addButton(new Button(startX + 161, startY, 15, 20, ">", button ->
        {
            if(currentIndex + 1 >= this.recipes.size())
            {
                this.loadItem(0);
            }
            else
            {
                this.loadItem(currentIndex + 1);
            }
        }));
        this.btnCraft = this.addButton(new Button(startX + 186, startY + 6, 97, 20, "Assemble", button ->
        {
            WorkbenchRecipe recipe = this.recipes.get(currentIndex);
            ResourceLocation registryName = recipe.getId();
            PacketHandler.getPlayChannel().sendToServer(new MessageCraft(registryName, this.workbench.getPos()));
        }));
        this.btnCraft.active = false;
        this.checkBoxMaterials = this.addButton(new CheckBox(startX + 186, startY + 90, "Show Remaining"));
        this.checkBoxMaterials.setToggled(WorkbenchScreen.showRemaining);
        this.loadItem(currentIndex);
    }

    @Override
    public void tick()
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

        this.btnCraft.active = canCraft;

        WorkbenchRecipe recipe = this.recipes.get(currentIndex);
        ItemStack item = recipe.getItem();
        if(item.getItem() instanceof ColoredItem)
        {
            ColoredItem colored = (ColoredItem) item.getItem();
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

        this.prevTransitionProgress = this.transitionProgress;

        if(this.transitioning)
        {
            if(this.transitionProgress > 0)
            {
                this.transitionProgress = Math.max(0, this.transitionProgress - 1);
            }
            else
            {
                this.transitioning = false;
            }
        }
        else if(this.transitionProgress < MAX_TRANSITION_TICKS)
        {
            this.transitionProgress = Math.min(MAX_TRANSITION_TICKS, this.transitionProgress + 1);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        WorkbenchScreen.showRemaining = this.checkBoxMaterials.isToggled();
        return result;
    }

    private void loadItem(int index)
    {
        previousIndex = currentIndex;
        this.prevDisplayProperty = this.displayProperty;

        WorkbenchRecipe recipe = this.recipes.get(index);
        ItemStack stack = recipe.getItem();

        this.materials.clear();
        this.displayProperty = RenderUtil.getModel(stack.getItem()).getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND);

        List<ItemStack> materials = recipe.getMaterials();
        if(materials != null)
        {
            for(ItemStack material : materials)
            {
                MaterialItem item = new MaterialItem(material);
                item.update();
                this.materials.add(item);
            }

            currentIndex = index;

            if(Config.CLIENT.display.workbenchAnimation.get() && previousIndex != currentIndex)
            {
                this.transitioning = true;
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
        for(int i = 0; i < this.filteredMaterials.size(); i++)
        {
            int itemX = startX + 186;
            int itemY = startY + i * 19 + 6 + 57;
            if(RenderUtil.isMouseWithin(mouseX, mouseY, itemX, itemY, 80, 19))
            {
                MaterialItem materialItem = this.filteredMaterials.get(i);
                if(!materialItem.getStack().isEmpty())
                {
                    this.renderTooltip(materialItem.getStack(), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        /* Fixes partial ticks to use percentage from 0 to 1 */
        partialTicks = Minecraft.getInstance().getRenderPartialTicks();

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        RenderSystem.enableBlend();

        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(startX, startY + 80, 0, 134, 176, 122);
        this.blit(startX + 180, startY, 176, 54, 6, 208);
        this.blit(startX + 186, startY, 182, 54, 57, 208);
        this.blit(startX + 186 + 57, startY, 220, 54, 23, 208);
        this.blit(startX + 186 + 57 + 23, startY, 220, 54, 3, 208);
        this.blit(startX + 186 + 57 + 23 + 3, startY, 236, 54, 20, 208);

        if(this.workbench.getStackInSlot(0).isEmpty())
        {
            this.blit(startX + 187, startY + 30, 80, 0, 16, 16);
        }

        WorkbenchRecipe recipe = this.recipes.get(currentIndex);
        ItemStack currentItem = recipe.getItem();
        StringBuilder builder = new StringBuilder(currentItem.getDisplayName().getUnformattedComponentText());
        if(currentItem.getCount() > 1)
        {
            builder.append(" x ");
            builder.append(currentItem.getCount());
        }
        this.drawCenteredString(this.font, builder.toString(), startX + 88, startY + 6, Color.WHITE.getRGB());

        RenderSystem.pushMatrix();
        {
            RenderSystem.translatef(startX + 88, startY + 60, 100);
            RenderSystem.scalef(90F, -90F, 90F);
            RenderSystem.rotatef(5F, 1, 0, 0);
            RenderSystem.rotatef(Minecraft.getInstance().player.ticksExisted + partialTicks, 0, 1, 0);

            RenderSystem.enableRescaleNormal();
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            MatrixStack matrixStack = new MatrixStack();
            IRenderTypeBuffer.Impl buffer = this.minecraft.getRenderTypeBuffers().getBufferSource();
            Minecraft.getInstance().getItemRenderer().renderItem(currentItem, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, RenderUtil.getModel(currentItem));
            buffer.finish();

            RenderSystem.disableAlphaTest();
            RenderSystem.disableRescaleNormal();
        }
        RenderSystem.popMatrix();

        this.filteredMaterials = this.getMaterials();
        for(int i = 0; i < this.filteredMaterials.size(); i++)
        {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.minecraft.getTextureManager().bindTexture(GUI);

            MaterialItem materialItem = this.filteredMaterials.get(i);
            ItemStack stack = materialItem.stack;
            if(stack.isEmpty())
            {
                RenderHelper.disableStandardItemLighting();
                this.blit(startX + 186, startY + i * 19 + 6 + 95, 0, 19, 80, 19);
            }
            else
            {
                RenderHelper.disableStandardItemLighting();
                if(materialItem.isEnabled())
                {
                    this.blit(startX + 186, startY + i * 19 + 6 + 95, 0, 0, 80, 19);
                }
                else
                {
                    this.blit(startX + 186, startY + i * 19 + 6 + 95, 0, 38, 80, 19);
                }

                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                String name = stack.getDisplayName().getUnformattedComponentText();
                if(this.font.getStringWidth(name) > 55)
                {
                    name = this.font.trimStringToWidth(name, 50).trim() + "...";
                }
                this.font.drawString(name, startX + 186 + 22, startY + i * 19 + 6 + 6 + 95, Color.WHITE.getRGB());

                Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, startX + 186 + 2, startY + i * 19 + 6 + 1 + 95);

                if(this.checkBoxMaterials.isToggled())
                {
                    int count = InventoryUtil.getItemStackAmount(Minecraft.getInstance().player, stack);
                    stack = stack.copy();
                    stack.setCount(stack.getCount() - count);
                }

                Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(this.font, stack, startX + 186 + 2, startY + i * 19 + 6 + 1 + 95, null);
            }
        }
    }

    private List<MaterialItem> getMaterials()
    {
        List<MaterialItem> materials = NonNullList.withSize(5, new MaterialItem(ItemStack.EMPTY));
        List<MaterialItem> filteredMaterials = this.materials.stream().filter(materialItem -> this.checkBoxMaterials.isToggled() ? !materialItem.isEnabled() : !materialItem.stack.isEmpty()).collect(Collectors.toList());
        for(int i = 0; i < filteredMaterials.size() && i < materials.size(); i++)
        {
            materials.set(i, filteredMaterials.get(i));
        }
        return materials;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, 109, 4210752);
    }

    public static class MaterialItem
    {
        public static final MaterialItem EMPTY = new MaterialItem();

        private boolean enabled = false;
        private ItemStack stack = ItemStack.EMPTY;

        private MaterialItem() {}

        private MaterialItem(ItemStack stack)
        {
            this.stack = stack;
        }

        public ItemStack getStack()
        {
            return stack;
        }

        public void update()
        {
            if(!this.stack.isEmpty())
            {
                this.enabled = InventoryUtil.hasItemStack(Minecraft.getInstance().player, this.stack);
            }
        }

        public boolean isEnabled()
        {
            return this.stack.isEmpty() || this.enabled;
        }
    }
}
