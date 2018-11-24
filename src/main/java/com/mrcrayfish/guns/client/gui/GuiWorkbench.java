package com.mrcrayfish.guns.client.gui;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.common.container.ContainerWorkbench;
import com.mrcrayfish.guns.init.ModCrafting;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.item.ItemColored;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageCraft;
import com.mrcrayfish.guns.tileentity.TileEntityWorkbench;
import com.mrcrayfish.guns.util.InventoryUtil;
import com.mrcrayfish.guns.util.ItemStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class GuiWorkbench extends GuiContainer
{
    private static final int MAX_TRANSITION_TICKS = 5;
    private static final ResourceLocation GUI = new ResourceLocation("cgm:textures/gui/workbench.png");
    private static final ImmutableMap<ItemStack, DisplayProperty> DISPLAY_PROPERTIES;

    static
    {
        ImmutableMap.Builder<ItemStack, DisplayProperty> builder = ImmutableMap.builder();
        registerGunDisplayProperty(builder, "handgun", new DisplayProperty(0.0F, 0.55F, -0.25F, 0.0F, 0.0F, 0.0F, 3.0F));
        registerGunDisplayProperty(builder, "shotgun", new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
        registerGunDisplayProperty(builder, "rifle", new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
        registerGunDisplayProperty(builder, "grenade_launcher", new DisplayProperty(0.0F, 0.55F, -0.1F, 0.0F, 0.0F, 0.0F, 3.0F));
        registerGunDisplayProperty(builder, "bazooka", new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.5F));
        registerGunDisplayProperty(builder, "chain_gun", new DisplayProperty(0.0F, 0.55F, 0.1F, 0.0F, 0.0F, 0.0F, 2.0F));
        registerGunDisplayProperty(builder, "assault_rifle", new DisplayProperty(0.0F, 0.55F, -0.15F, 0.0F, 0.0F, 0.0F, 3.0F));
        builder.put(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.BASIC.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
        builder.put(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.ADVANCED.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
        builder.put(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.SHELL.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5F));
        builder.put(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.GRENADE.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
        builder.put(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.MISSILE.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F));
        builder.put(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.SMALL.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
        builder.put(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.MEDIUM.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
        builder.put(new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.LONG.ordinal()), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
        builder.put(new ItemStack(ModGuns.SILENCER), new DisplayProperty(0.0F, 0.25F, 0.5F, 0.0F, 0.0F, 0.0F, 1.5F));
        DISPLAY_PROPERTIES = builder.build();
    }

    private static void registerGunDisplayProperty(ImmutableMap.Builder<ItemStack, DisplayProperty> builder, String id, DisplayProperty property)
    {
        ItemGun gun = ModGuns.getGun(id);
        if(gun != null)
        {
            builder.put(new ItemStack(gun), property);
        }
    }

    @Nullable
    private static DisplayProperty getDisplayProperty(ItemStack find)
    {
        for(ItemStack stack : DISPLAY_PROPERTIES.keySet())
        {
            if(ItemStackHelper.areItemStackSameItem(stack, find))
            {
                return DISPLAY_PROPERTIES.get(stack);
            }
        }
        return null;
    }

    private List<MaterialItem> materials;
    private static int currentIndex = 0;
    private static int previousIndex = 0;
    private static boolean showRemaining = false;
    private NonNullList<ItemStack> cachedItems;
    private IInventory playerInventory;
    private TileEntityWorkbench workbench;
    private GuiButton btnCraft;
    private GuiCheckBox checkBoxMaterials;
    private boolean transitioning;
    private int transitionProgress = MAX_TRANSITION_TICKS;
    private int prevTransitionProgress = MAX_TRANSITION_TICKS;
    private DisplayProperty displayProperty;
    private DisplayProperty prevDisplayProperty;

    public GuiWorkbench(IInventory playerInventory, TileEntityWorkbench workbench)
    {
        super(new ContainerWorkbench(playerInventory, workbench));
        this.playerInventory = playerInventory;
        this.workbench = workbench;
        this.xSize = 289;
        this.ySize = 202;
        this.materials = new ArrayList<>();
        this.cachedItems = NonNullList.withSize(ModCrafting.getRecipeMaterials().size(), ItemStack.EMPTY);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(1, startX, startY, 15, 20, "<"));
        this.buttonList.add(new GuiButton(2, startX + 161, startY, 15, 20, ">"));
        this.buttonList.add(btnCraft = new GuiButton(3, startX + 186, startY + 6, 97, 20, "Assemble"));
        this.btnCraft.enabled = false;
        this.checkBoxMaterials = new GuiCheckBox(186, 90, "Show Remaining");
        this.checkBoxMaterials.setToggled(GuiWorkbench.showRemaining);
        this.loadItem(currentIndex);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        for(MaterialItem material : materials)
        {
            material.update();
        }

        boolean canCraft = true;
        for(MaterialItem material : materials)
        {
            if(!material.isEnabled())
            {
                canCraft = false;
                break;
            }
        }

        btnCraft.enabled = canCraft;

        ItemStack currentWeapon = cachedItems.get(currentIndex);
        if(!currentWeapon.isEmpty())
        {
            Item item = currentWeapon.getItem();
            if(item instanceof ItemColored)
            {
                ItemColored colored = (ItemColored) item;
                if(!workbench.getStackInSlot(0).isEmpty())
                {
                    ItemStack dyeStack = workbench.getStackInSlot(0);
                    if(dyeStack.getItem() == Items.DYE)
                    {
                        Optional<EnumDyeColor> optional = DyeUtils.colorFromStack(dyeStack);
                        if(optional.isPresent())
                        {
                            float[] color = optional.get().getColorComponentValues();
                            int red = (int) (color[0] * 255F);
                            int green = (int) (color[1] * 255F);
                            int blue = (int) (color[2] * 255F);
                            colored.setColor(currentWeapon, ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF)));
                        }
                    }
                    else
                    {
                        colored.removeColor(currentWeapon);
                    }
                }
                else
                {
                    colored.removeColor(currentWeapon);
                }
            }
        }

        prevTransitionProgress = transitionProgress;
        if(transitioning)
        {
            if(transitionProgress > 0)
            {
                transitionProgress = Math.max(0, transitionProgress - 1);
            }
            else
            {
                transitioning = false;
            }
        }
        else if(transitionProgress < MAX_TRANSITION_TICKS)
        {
            transitionProgress = Math.min(MAX_TRANSITION_TICKS, transitionProgress + 1);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        this.checkBoxMaterials.handleClick(startX, startY, mouseX, mouseY, mouseButton);
        GuiWorkbench.showRemaining = this.checkBoxMaterials.isToggled();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if(button.id == 1)
        {
            if(currentIndex - 1 < 0)
            {
                this.loadItem(ModCrafting.getRecipeMaterials().size() - 1);
            }
            else
            {
                this.loadItem(currentIndex - 1);
            }
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
        else if(button.id == 2)
        {
            if(currentIndex + 1 >= ModCrafting.getRecipeMaterials().size())
            {
                this.loadItem(0);
            }
            else
            {
                this.loadItem(currentIndex + 1);
            }
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
        else if(button.id == 3)
        {
            ItemStack currentWeapon = cachedItems.get(currentIndex);
            if(!currentWeapon.isEmpty())
            {
                ResourceLocation registryName = currentWeapon.getItem().getRegistryName();
                if(registryName != null)
                {
                    IMessage message = new MessageCraft(currentWeapon, workbench.getPos());
                    PacketHandler.INSTANCE.sendToServer(message);
                }
            }
        }
    }

    private void loadItem(int index)
    {
        previousIndex = currentIndex;
        prevDisplayProperty = displayProperty;

        ItemStack stack = ModCrafting.getRecipeMaterials().keySet().asList().get(index);
        if(cachedItems.get(index).isEmpty())
        {
            cachedItems.set(index, stack.copy());
        }

        if(stack != null)
        {
            materials.clear();

            displayProperty = getDisplayProperty(stack);

            List<ItemStack> materials = ModCrafting.getMaterialsForStack(stack);
            if(materials != null)
            {
                for(ItemStack material : materials)
                {
                    MaterialItem item = new MaterialItem(material);
                    item.update();
                    this.materials.add(item);
                }

                currentIndex = index;

                if(GunConfig.CLIENT.display.workbenchAnimation && previousIndex != currentIndex)
                {
                    transitioning = true;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        //TODO add hover tool tip to material list
        //this.renderToolTip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        /* Fixes partial ticks to use percentage from 0 to 1 */
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

        this.drawDefaultBackground();

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        GlStateManager.enableBlend();

        this.mc.getTextureManager().bindTexture(GUI);
        this.drawTexturedModalRect(startX, startY + 80, 0, 134, 176, 122);
        this.drawTexturedModalRect(startX + 180, startY, 176, 54, 6, 208);
        this.drawTexturedModalRect(startX + 186, startY, 182, 54, 57, 208);
        this.drawTexturedModalRect(startX + 186 + 57, startY, 220, 54, 23, 208);
        this.drawTexturedModalRect(startX + 186 + 57 + 23, startY, 220, 54, 3, 208);
        this.drawTexturedModalRect(startX + 186 + 57 + 23 + 3, startY, 236, 54, 20, 208);

        if(workbench.getStackInSlot(0).isEmpty())
        {
            this.drawTexturedModalRect(startX + 187, startY + 30, 80, 0, 16, 16);
        }

        this.checkBoxMaterials.draw(mc, guiLeft, guiTop);

        ItemStack currentItem = cachedItems.get(currentIndex);
        StringBuilder builder = new StringBuilder(currentItem.getDisplayName());
        if(currentItem.getCount() > 1)
        {
            builder.append(" x ");
            builder.append(currentItem.getCount());
        }
        this.drawCenteredString(fontRenderer, builder.toString(), startX + 88, startY + 6, Color.WHITE.getRGB());

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(startX + 88, startY + 90, 100);

            float transitionPercent = (prevTransitionProgress + (transitionProgress - prevTransitionProgress) * partialTicks) / (float) MAX_TRANSITION_TICKS;
            float scale = 40F * transitionPercent;
            GlStateManager.scale(scale, -scale, scale);

            GlStateManager.rotate(5F, 1, 0, 0);
            GlStateManager.rotate(Minecraft.getMinecraft().player.ticksExisted + partialTicks, 0, 1, 0);

            DisplayProperty property = transitioning ? prevDisplayProperty : displayProperty;
            if(property != null)
            {
                GlStateManager.scale(property.getScale(), property.getScale(), property.getScale());
                GlStateManager.rotate((float) property.getRotX(), 1, 0, 0);
                GlStateManager.rotate((float) property.getRotY(), 0, 1, 0);
                GlStateManager.rotate((float) property.getRotZ(), 0, 0, 1);
                GlStateManager.translate(property.getX(), property.getY(), property.getZ());
            }

            int vehicleIndex = transitioning ? previousIndex : currentIndex;
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(cachedItems.get(vehicleIndex), ItemCameraTransforms.TransformType.NONE);
        }
        GlStateManager.popMatrix();

        List<MaterialItem> materials = this.getMaterials();
        for(int i = 0; i < materials.size(); i++)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(GUI);

            MaterialItem materialItem = materials.get(i);
            ItemStack stack = materialItem.stack;
            if(stack.isEmpty())
            {
                RenderHelper.disableStandardItemLighting();
                this.drawTexturedModalRect(startX + 186, startY + i * 19 + 6 + 95, 0, 19, 80, 19);
            }
            else
            {
                RenderHelper.disableStandardItemLighting();
                if(materialItem.isEnabled())
                {
                    this.drawTexturedModalRect(startX + 186, startY + i * 19 + 6 + 95, 0, 0, 80, 19);
                }
                else
                {
                    this.drawTexturedModalRect(startX + 186, startY + i * 19 + 6 + 95, 0, 38, 80, 19);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                String name = stack.getDisplayName();
                if(fontRenderer.getStringWidth(name) > 55)
                {
                    name = fontRenderer.trimStringToWidth(stack.getDisplayName(), 50).trim() + "...";
                }
                fontRenderer.drawString(name, startX + 186 + 22, startY + i * 19 + 6 + 6 + 95, Color.WHITE.getRGB());

                RenderHelper.enableGUIStandardItemLighting();
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, startX + 186 + 2, startY + i * 19 + 6 + 1 + 95);

                if(checkBoxMaterials.isToggled())
                {
                    int count = InventoryUtil.getItemStackAmount(Minecraft.getMinecraft().player, stack);
                    stack = stack.copy();
                    stack.setCount(stack.getCount() - count);
                }

                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(fontRenderer, stack, startX + 186 + 2, startY + i * 19 + 6 + 1 + 95, null);
            }
        }
    }

    private List<MaterialItem> getMaterials()
    {
        List<MaterialItem> materials = NonNullList.withSize(5, new MaterialItem(ItemStack.EMPTY));
        List<MaterialItem> filteredMaterials = this.materials.stream().filter(materialItem -> checkBoxMaterials.isToggled() ? !materialItem.isEnabled() : !materialItem.stack.isEmpty()).collect(Collectors.toList());
        for(int i = 0; i < filteredMaterials.size() && i < materials.size(); i++)
        {
            materials.set(i, filteredMaterials.get(i));
        }
        return materials;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, 109, 4210752);
    }

    public static class MaterialItem extends Gui
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
            if(!stack.isEmpty())
            {
                enabled = InventoryUtil.hasItemStack(Minecraft.getMinecraft().player, stack);
            }
        }

        public boolean isEnabled()
        {
            return stack.isEmpty() || enabled;
        }
    }
}
