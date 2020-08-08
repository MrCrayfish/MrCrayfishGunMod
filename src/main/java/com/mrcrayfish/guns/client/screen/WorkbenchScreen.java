package com.mrcrayfish.guns.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.common.container.WorkbenchContainer;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAmmo;
import com.mrcrayfish.guns.item.IColored;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageCraft;
import com.mrcrayfish.guns.tileentity.WorkbenchTileEntity;
import com.mrcrayfish.guns.util.InventoryUtil;
import com.mrcrayfish.guns.util.ItemStackUtil;
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
 * Author: MrCrayfish
 */
public class WorkbenchScreen extends ContainerScreen<WorkbenchContainer>
{
    private static final ResourceLocation GUI_BASE = new ResourceLocation("cgm:textures/gui/workbench.png");
    private static boolean showRemaining = false;

    private Tab currentTab;
    private List<Tab> tabs = new ArrayList<>();
    private List<MaterialItem> materials;
    private List<MaterialItem> filteredMaterials;
    private PlayerInventory playerInventory;
    private WorkbenchTileEntity workbench;
    private Button btnCraft;
    private CheckBox checkBoxMaterials;
    private ItemStack displayStack;

    public WorkbenchScreen(WorkbenchContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.workbench = container.getWorkbench();
        this.xSize = 256;
        this.ySize = 184;
        this.materials = new ArrayList<>();
        this.createTabs(WorkbenchRecipes.getAll(playerInventory.player.world));
        if(!this.tabs.isEmpty())
        {
            this.ySize += 28;
        }
    }

    private void createTabs(NonNullList<WorkbenchRecipe> recipes)
    {
        List<WorkbenchRecipe> weapons = new ArrayList<>();
        List<WorkbenchRecipe> attachments = new ArrayList<>();
        List<WorkbenchRecipe> ammo = new ArrayList<>();
        List<WorkbenchRecipe> misc = new ArrayList<>();

        for(WorkbenchRecipe recipe : recipes)
        {
            ItemStack output = recipe.getItem();
            if(output.getItem() instanceof GunItem)
            {
                weapons.add(recipe);
            }
            else if(output.getItem() instanceof IAttachment)
            {
                attachments.add(recipe);
            }
            else if(this.isAmmo(output))
            {
                ammo.add(recipe);
            }
            else
            {
                misc.add(recipe);
            }
        }

        if(!weapons.isEmpty())
        {
            ItemStack icon = new ItemStack(ModItems.ASSAULT_RIFLE.get());
            ItemStackUtil.createTagCompound(icon).putInt("AmmoCount", ModItems.ASSAULT_RIFLE.get().getGun().getGeneral().getMaxAmmo());
            this.tabs.add(new Tab(icon, "weapons", weapons));
        }

        if(!attachments.isEmpty())
        {
            this.tabs.add(new Tab(new ItemStack(ModItems.LONG_SCOPE.get()), "attachments", attachments));
        }

        if(!ammo.isEmpty())
        {
            this.tabs.add(new Tab(new ItemStack(ModItems.SHELL.get()), "ammo", ammo));
        }

        if(!misc.isEmpty())
        {
            this.tabs.add(new Tab(new ItemStack(Items.BARRIER), "misc", misc));
        }

        if(!this.tabs.isEmpty())
        {
            this.currentTab = this.tabs.get(0);
        }
    }

    private boolean isAmmo(ItemStack stack)
    {
        if(stack.getItem() instanceof IAmmo)
        {
            return true;
        }
        ResourceLocation id = stack.getItem().getRegistryName();
        Objects.requireNonNull(id);
        for(GunItem gunItem : NetworkGunManager.getClientRegisteredGuns())
        {
            if(id.equals(gunItem.getModifiedGun(stack).getProjectile().getItem()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init()
    {
        super.init();
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        if(!this.tabs.isEmpty())
        {
            startY += 28;
        }
        this.addButton(new Button(startX + 9, startY + 18, 15, 20, new StringTextComponent("<"), button ->
        {
            int index = this.currentTab.getCurrentIndex();
            if(index - 1 < 0)
            {
                this.loadItem(this.currentTab.getRecipes().size() - 1);
            }
            else
            {
                this.loadItem(index - 1);
            }
        }));
        this.addButton(new Button(startX + 153, startY + 18, 15, 20, new StringTextComponent(">"), button ->
        {
            int index = this.currentTab.getCurrentIndex();
            if(index + 1 >= this.currentTab.getRecipes().size())
            {
                this.loadItem(0);
            }
            else
            {
                this.loadItem(index + 1);
            }
        }));
        this.btnCraft = this.addButton(new Button(startX + 195, startY + 16, 74, 20, new TranslationTextComponent("gui.cgm.workbench.assemble"), button ->
        {
            int index = this.currentTab.getCurrentIndex();
            WorkbenchRecipe recipe = this.currentTab.getRecipes().get(index);
            ResourceLocation registryName = recipe.getId();
            PacketHandler.getPlayChannel().sendToServer(new MessageCraft(registryName, this.workbench.getPos()));
        }));
        this.btnCraft.active = false;
        this.checkBoxMaterials = this.addButton(new CheckBox(startX + 172, startY + 51, new TranslationTextComponent("gui.cgm.workbench.show_remaining")));
        this.checkBoxMaterials.setToggled(WorkbenchScreen.showRemaining);
        this.loadItem(this.currentTab.getCurrentIndex());
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
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        WorkbenchScreen.showRemaining = this.checkBoxMaterials.isToggled();

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        for(int i = 0; i < this.tabs.size(); i++)
        {
            if(RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, startX + 28 * i, startY, 28, 28))
            {
                this.currentTab = this.tabs.get(i);
                this.loadItem(this.currentTab.getCurrentIndex());
                this.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }

        return result;
    }

    private void loadItem(int index)
    {
        WorkbenchRecipe recipe = this.currentTab.getRecipes().get(index);
        this.displayStack = recipe.getItem().copy();
        this.updateColor();

        this.materials.clear();

        List<ItemStack> materials = recipe.getMaterials();
        if(materials != null)
        {
            for(ItemStack material : materials)
            {
                MaterialItem item = new MaterialItem(material);
                item.update();
                this.materials.add(item);
            }

            this.currentTab.setCurrentIndex(index);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        for(int i = 0; i < this.tabs.size(); i++)
        {
            if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 28 * i, startY, 28, 28))
            {
                this.renderTooltip(matrixStack, new TranslationTextComponent(this.tabs.get(i).getTabKey()), mouseX, mouseY);
                return;
            }
        }

        if(!this.tabs.isEmpty())
        {
            startY += 28;
        }

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

        if(RenderUtil.isMouseWithin(mouseX, mouseY, startX + 8, startY + 38, 160, 48))
        {
            this.renderTooltip(matrixStack, this.displayStack, mouseX, mouseY);
        }
    }

    @Override
    protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        int offset = this.tabs.isEmpty() ? 0 : 28;
        this.font.func_238422_b_(matrixStack, this.title, (float)this.field_238744_r_, (float)this.field_238745_s_ - 66 + offset, 4210752);
        this.font.func_238422_b_(matrixStack, this.playerInventory.getDisplayName(), (float)this.field_238744_r_, (float)this.field_238745_s_ + 19 + offset, 4210752);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        /* Fixes partial ticks to use percentage from 0 to 1 */
        partialTicks = Minecraft.getInstance().getRenderPartialTicks();

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        if(!this.tabs.isEmpty())
        {
            startY += 28;
        }

        RenderSystem.enableBlend();

        /* Draw unselected tabs */
        for(int i = 0; i < this.tabs.size(); i++)
        {
            Tab tab = this.tabs.get(i);
            if(tab != this.currentTab)
            {
                this.minecraft.getTextureManager().bindTexture(GUI_BASE);
                this.blit(matrixStack, startX + 28 * i, startY - 28, 80, 184, 28, 32);
                Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(tab.getIcon(), startX + 28 * i + 6, startY - 28 + 8);
                Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(this.font, tab.getIcon(), startX + 28 * i + 6, startY - 28 + 8, null);
            }
        }

        this.minecraft.getTextureManager().bindTexture(GUI_BASE);
        this.blit(matrixStack, startX, startY, 0, 0, 173, 184);
        blit(matrixStack, startX + 173, startY, 78, 184, 173, 0, 1, 184, 256, 256);
        this.blit(matrixStack, startX + 251, startY, 174, 0, 24, 184);
        this.blit(matrixStack, startX + 172, startY + 16, 198, 0, 20, 20);

        /* Draw selected tab */
        if(this.currentTab != null)
        {
            int i = this.tabs.indexOf(this.currentTab);
            int u = i == 0 ? 80 : 108;
            this.minecraft.getTextureManager().bindTexture(GUI_BASE);
            this.blit(matrixStack, startX + 28 * i, startY - 28, u, 214, 28, 32);
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(this.currentTab.getIcon(), startX + 28 * i + 6, startY - 28 + 8);
            Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(this.font, this.currentTab.getIcon(), startX + 28 * i + 6, startY - 28 + 8, null);
        }

        this.minecraft.getTextureManager().bindTexture(GUI_BASE);

        if(this.workbench.getStackInSlot(0).isEmpty())
        {
            this.blit(matrixStack, startX + 174, startY + 18, 165, 199, 16, 16);
        }

        ItemStack currentItem = this.displayStack;
        StringBuilder builder = new StringBuilder(currentItem.getDisplayName().getString());
        if(currentItem.getCount() > 1)
        {
            builder.append(TextFormatting.GOLD);
            builder.append(TextFormatting.BOLD);
            builder.append(" x ");
            builder.append(currentItem.getCount());
        }
        this.drawCenteredString(matrixStack, this.font, builder.toString(), startX + 88, startY + 22, Color.WHITE.getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(startX + 8, startY + 17, 160, 70);

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

            IRenderTypeBuffer.Impl buffer = this.minecraft.getRenderTypeBuffers().getBufferSource();
            Minecraft.getInstance().getItemRenderer().renderItem(currentItem, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, RenderUtil.getModel(currentItem));
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

                Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(this.font, stack, startX + 172 + 2, startY + i * 19 + 1 + 63, null);
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

    private static class Tab
    {
        private final ItemStack icon;
        private final String id;
        private final List<WorkbenchRecipe> items;
        private int currentIndex;

        public Tab(ItemStack icon, String id, List<WorkbenchRecipe> items)
        {
            this.icon = icon;
            this.id = id;
            this.items = items;
        }

        public ItemStack getIcon()
        {
            return this.icon;
        }

        public String getTabKey()
        {
            return "gui.cgm.workbench.tab." + this.id;
        }

        public void setCurrentIndex(int currentIndex)
        {
            this.currentIndex = currentIndex;
        }

        public int getCurrentIndex()
        {
            return this.currentIndex;
        }

        public List<WorkbenchRecipe> getRecipes()
        {
            return this.items;
        }
    }
}
