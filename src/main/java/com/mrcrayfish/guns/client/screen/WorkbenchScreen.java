package com.mrcrayfish.guns.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.guns.blockentity.WorkbenchBlockEntity;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.common.container.WorkbenchContainer;
import com.mrcrayfish.guns.crafting.WorkbenchIngredient;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAmmo;
import com.mrcrayfish.guns.item.IColored;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.C2SMessageCraft;
import com.mrcrayfish.guns.util.InventoryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 */
public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchContainer> {
    private static final ResourceLocation GUI_BASE = new ResourceLocation("cgm:textures/gui/workbench.png");
    private static boolean showRemaining = false;

    private Tab currentTab;
    private List<Tab> tabs = new ArrayList<>();
    private List<MaterialItem> materials;
    private List<MaterialItem> filteredMaterials;
    private Inventory playerInventory;
    private WorkbenchBlockEntity workbench;
    private Button btnCraft;
    private CheckBox checkBoxMaterials;
    private ItemStack displayStack;

    public WorkbenchScreen(WorkbenchContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.workbench = container.getWorkbench();
        this.imageWidth = 275;
        this.imageHeight = 184;
        this.materials = new ArrayList<>();
        this.createTabs(WorkbenchRecipes.getAll(playerInventory.player.level()));
        if (!this.tabs.isEmpty()) {
            this.imageHeight += 28;
        }
    }

    private void createTabs(NonNullList<WorkbenchRecipe> recipes) {
        List<WorkbenchRecipe> weapons = new ArrayList<>();
        List<WorkbenchRecipe> attachments = new ArrayList<>();
        List<WorkbenchRecipe> ammo = new ArrayList<>();
        List<WorkbenchRecipe> misc = new ArrayList<>();

        for (WorkbenchRecipe recipe : recipes) {
            ItemStack output = recipe.getItem();
            if (output.getItem() instanceof GunItem) {
                weapons.add(recipe);
            } else if (output.getItem() instanceof IAttachment) {
                attachments.add(recipe);
            } else if (this.isAmmo(output)) {
                ammo.add(recipe);
            } else {
                misc.add(recipe);
            }
        }

        if (!weapons.isEmpty()) {
            ItemStack icon = new ItemStack(ModItems.ASSAULT_RIFLE.get());
            icon.getOrCreateTag().putInt("AmmoCount", ModItems.ASSAULT_RIFLE.get().getGun().getGeneral().getMaxAmmo());
            this.tabs.add(new Tab(icon, "weapons", weapons));
        }

        if (!attachments.isEmpty()) {
            this.tabs.add(new Tab(new ItemStack(ModItems.LONG_SCOPE.get()), "attachments", attachments));
        }

        if (!ammo.isEmpty()) {
            this.tabs.add(new Tab(new ItemStack(ModItems.SHELL.get()), "ammo", ammo));
        }

        if (!misc.isEmpty()) {
            this.tabs.add(new Tab(new ItemStack(Items.BARRIER), "misc", misc));
        }

        if (!this.tabs.isEmpty()) {
            this.currentTab = this.tabs.get(0);
        }
    }

    private boolean isAmmo(ItemStack stack) {
        if (stack.getItem() instanceof IAmmo) {
            return true;
        }
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        Objects.requireNonNull(id);
        for (GunItem gunItem : NetworkGunManager.getClientRegisteredGuns()) {
            if (id.equals(gunItem.getModifiedGun(stack).getProjectile().getItem())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init() {
        super.init();
        if (!this.tabs.isEmpty()) {
            this.topPos += 28;
        }
        this.addRenderableWidget(Button.builder(Component.literal("<"), button ->
        {
            int index = this.currentTab.getCurrentIndex();
            if (index - 1 < 0) {
                this.loadItem(this.currentTab.getRecipes().size() - 1);
            } else {
                this.loadItem(index - 1);
            }
        }).pos(this.leftPos + 9, this.topPos + 18).size(15, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), button ->
        {
            int index = this.currentTab.getCurrentIndex();
            if (index + 1 >= this.currentTab.getRecipes().size()) {
                this.loadItem(0);
            } else {
                this.loadItem(index + 1);
            }
        }).pos(this.leftPos + 153, this.topPos + 18).size(15, 20).build());
        this.btnCraft = this.addRenderableWidget(Button.builder(Component.translatable("gui.cgm.workbench.assemble"), button ->
        {
            int index = this.currentTab.getCurrentIndex();
            WorkbenchRecipe recipe = this.currentTab.getRecipes().get(index);
            ResourceLocation registryName = recipe.getId();
            PacketHandler.getPlayChannel().sendToServer(new C2SMessageCraft(registryName, this.workbench.getBlockPos()));
        }).pos(this.leftPos + 195, this.topPos + 16).size(74, 20).build());
        this.btnCraft.active = false;
        this.checkBoxMaterials = this.addRenderableWidget(new CheckBox(this.leftPos + 172, this.topPos + 51, Component.translatable("gui.cgm.workbench.show_remaining")));
        this.checkBoxMaterials.setToggled(WorkbenchScreen.showRemaining);
        this.loadItem(this.currentTab.getCurrentIndex());
    }

    @Override
    public void containerTick() {
        super.containerTick();

        for (MaterialItem material : this.materials) {
            material.tick();
        }

        boolean canCraft = true;
        for (MaterialItem material : this.materials) {
            if (!material.isEnabled()) {
                canCraft = false;
                break;
            }
        }

        this.btnCraft.active = canCraft;
        this.updateColor();
    }

    private void updateColor() {
        if (this.currentTab != null) {
            ItemStack item = this.displayStack;
            if (IColored.isDyeable(item)) {
                IColored colored = (IColored) item.getItem();
                if (!this.workbench.getItem(0).isEmpty()) {
                    ItemStack dyeStack = this.workbench.getItem(0);
                    if (dyeStack.getItem() instanceof DyeItem) {
                        DyeColor color = ((DyeItem) dyeStack.getItem()).getDyeColor();
                        float[] components = color.getTextureDiffuseColors();
                        int red = (int) (components[0] * 255F);
                        int green = (int) (components[1] * 255F);
                        int blue = (int) (components[2] * 255F);
                        colored.setColor(item, ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF)));
                    } else {
                        colored.removeColor(item);
                    }
                } else {
                    colored.removeColor(item);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        WorkbenchScreen.showRemaining = this.checkBoxMaterials.isToggled();

        for (int i = 0; i < this.tabs.size(); i++) {
            if (RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, this.leftPos + 28 * i, this.topPos - 28, 28, 28)) {
                this.currentTab = this.tabs.get(i);
                this.loadItem(this.currentTab.getCurrentIndex());
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }

        return result;
    }

    private void loadItem(int index) {
        WorkbenchRecipe recipe = this.currentTab.getRecipes().get(index);
        this.displayStack = recipe.getItem().copy();
        this.updateColor();

        this.materials.clear();

        List<WorkbenchIngredient> ingredients = recipe.getMaterials();
        if (ingredients != null) {
            for (WorkbenchIngredient ingredient : ingredients) {
                MaterialItem item = new MaterialItem(ingredient);
                item.updateEnabledState();
                this.materials.add(item);
            }

            this.currentTab.setCurrentIndex(index);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);

        int startX = this.leftPos;
        int startY = this.topPos;

        for (int i = 0; i < this.tabs.size(); i++) {
            if (RenderUtil.isMouseWithin(mouseX, mouseY, startX + 28 * i, startY - 28, 28, 28)) {
                this.setTooltipForNextRenderPass(Component.translatable(this.tabs.get(i).getTabKey()));
                this.renderTooltip(graphics, mouseX, mouseY);
                return;
            }
        }

        for (int i = 0; i < this.filteredMaterials.size(); i++) {
            int itemX = startX + 172;
            int itemY = startY + i * 19 + 63;
            if (RenderUtil.isMouseWithin(mouseX, mouseY, itemX, itemY, 80, 19)) {
                MaterialItem materialItem = this.filteredMaterials.get(i);
                if (materialItem != MaterialItem.EMPTY) {
                    graphics.renderTooltip(this.font, materialItem.getDisplayStack(), mouseX, mouseY);
                    return;
                }
            }
        }

        if (RenderUtil.isMouseWithin(mouseX, mouseY, startX + 8, startY + 38, 160, 48)) {
            graphics.renderTooltip(this.font, this.displayStack, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int offset = this.tabs.isEmpty() ? 0 : 28;
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY - 28 + offset, 4210752, false);
        graphics.drawString(this.font, this.playerInventory.getDisplayName(), this.inventoryLabelX, this.inventoryLabelY - 9 + offset, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        /* Fixes partial ticks to use percentage from 0 to 1 */
        partialTicks = Minecraft.getInstance().getFrameTime();

        int startX = this.leftPos;
        int startY = this.topPos;

        RenderSystem.enableBlend();

        /* Draw unselected tabs */
        for (int i = 0; i < this.tabs.size(); i++) {
            Tab tab = this.tabs.get(i);
            if (tab != this.currentTab) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(GUI_BASE, startX + 28 * i, startY - 28, 80, 184, 28, 32);
                graphics.renderItem(tab.getIcon(), startX + 28 * i + 6, startY - 28 + 8);
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(GUI_BASE, startX, startY, 0, 0, 173, 184);
        graphics.blit(GUI_BASE, startX + 173, startY, 78, 184, 173, 0, 1, 184, 256, 256);
        graphics.blit(GUI_BASE, startX + 251, startY, 174, 0, 24, 184);
        graphics.blit(GUI_BASE, startX + 172, startY + 16, 198, 0, 20, 20);

        /* Draw selected tab */
        if (this.currentTab != null) {
            int i = this.tabs.indexOf(this.currentTab);
            int u = i == 0 ? 80 : 108;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(GUI_BASE, startX + 28 * i, startY - 28, u, 214, 28, 32);
            graphics.renderItem(this.currentTab.getIcon(), startX + 28 * i + 6, startY - 28 + 8);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.workbench.getItem(0).isEmpty()) {
            graphics.blit(GUI_BASE, startX + 174, startY + 18, 165, 199, 16, 16);
        }

        ItemStack currentItem = this.displayStack;
        StringBuilder builder = new StringBuilder(currentItem.getHoverName().getString());
        if (currentItem.getCount() > 1) {
            builder.append(ChatFormatting.GOLD);
            builder.append(ChatFormatting.BOLD);
            builder.append(" x ");
            builder.append(currentItem.getCount());
        }
        graphics.drawCenteredString(this.font, builder.toString(), startX + 88, startY + 22, Color.WHITE.getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(startX + 8, startY + 17, 160, 70);

        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX + 88, startY + 60, 100);
            modelViewStack.scale(50F, -50F, 50F);
            modelViewStack.mulPose(Axis.XP.rotationDegrees(5F));
            modelViewStack.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().player.tickCount + partialTicks));
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = this.minecraft.renderBuffers().bufferSource();
            Minecraft.getInstance().getItemRenderer().render(currentItem, ItemDisplayContext.FIXED, false, graphics.pose(), buffer, 15728880, OverlayTexture.NO_OVERLAY, RenderUtil.getModel(currentItem));
            buffer.endBatch();
        }
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        this.filteredMaterials = this.getMaterials();
        for (int i = 0; i < this.filteredMaterials.size(); i++) {
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

            MaterialItem materialItem = this.filteredMaterials.get(i);
            ItemStack stack = materialItem.getDisplayStack();
            if (!stack.isEmpty()) {
                Lighting.setupForFlatItems();
                if (materialItem.isEnabled()) {
                    graphics.blit(GUI_BASE,startX + 172, startY + i * 19 + 63, 0, 184, 80, 19);
                } else {
                    graphics.blit(GUI_BASE,startX + 172, startY + i * 19 + 63, 0, 222, 80, 19);
                }

                String name = stack.getHoverName().getString();
                if (this.font.width(name) > 55) {
                    name = this.font.plainSubstrByWidth(name, 50).trim() + "...";
                }
                graphics.drawString(this.font, name, startX + 172 + 22, startY + i * 19 + 6 + 63, Color.WHITE.getRGB());

                graphics.renderItem(stack, startX + 172 + 2, startY + i * 19 + 1 + 63);

                if (this.checkBoxMaterials.isToggled()) {
                    int count = InventoryUtil.getItemStackAmount(Minecraft.getInstance().player, stack);
                    stack = stack.copy();
                    stack.setCount(stack.getCount() - count);
                }

                graphics.renderItemDecorations(this.font, stack, startX + 172 + 2, startY + i * 19 + 1 + 63);
            }
        }
    }

    private List<MaterialItem> getMaterials() {
        List<MaterialItem> materials = NonNullList.withSize(6, MaterialItem.EMPTY);
        List<MaterialItem> filteredMaterials = this.materials.stream().filter(materialItem -> this.checkBoxMaterials.isToggled() ? !materialItem.isEnabled() : materialItem != MaterialItem.EMPTY).collect(Collectors.toList());
        for (int i = 0; i < filteredMaterials.size() && i < materials.size(); i++) {
            materials.set(i, filteredMaterials.get(i));
        }
        return materials;
    }

    public List<Tab> getTabs() {
        return ImmutableList.copyOf(this.tabs);
    }

    public static class MaterialItem {
        public static final MaterialItem EMPTY = new MaterialItem();
        private final List<ItemStack> displayStacks = new ArrayList<>();
        private long lastTime = System.currentTimeMillis();
        private int displayIndex;
        private boolean enabled = false;
        private WorkbenchIngredient ingredient;

        private MaterialItem() {
        }

        private MaterialItem(WorkbenchIngredient ingredient) {
            this.ingredient = ingredient;
            Stream.of(ingredient.getItems()).forEach(stack -> {
                ItemStack displayStack = stack.copy();
                displayStack.setCount(ingredient.getCount());
                this.displayStacks.add(displayStack);
            });
        }

        public WorkbenchIngredient getIngredient() {
            return this.ingredient;
        }

        public void tick() {
            if (this.ingredient == null)
                return;

            this.updateEnabledState();
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.lastTime >= 1000) {
                this.displayIndex = (this.displayIndex + 1) % this.displayStacks.size();
                this.lastTime = currentTime;
            }
        }

        public ItemStack getDisplayStack() {
            return this.ingredient != null ? this.displayStacks.get(this.displayIndex) : ItemStack.EMPTY;
        }

        public void updateEnabledState() {
            this.enabled = InventoryUtil.hasWorkstationIngredient(Minecraft.getInstance().player, this.ingredient);
        }

        public boolean isEnabled() {
            return this.ingredient == null || this.enabled;
        }
    }

    private static class Tab {
        private final ItemStack icon;
        private final String id;
        private final List<WorkbenchRecipe> items;
        private int currentIndex;

        public Tab(ItemStack icon, String id, List<WorkbenchRecipe> items) {
            this.icon = icon;
            this.id = id;
            this.items = items;
        }

        public ItemStack getIcon() {
            return this.icon;
        }

        public String getTabKey() {
            return "gui.cgm.workbench.tab." + this.id;
        }

        public int getCurrentIndex() {
            return this.currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        public List<WorkbenchRecipe> getRecipes() {
            return this.items;
        }
    }
}
