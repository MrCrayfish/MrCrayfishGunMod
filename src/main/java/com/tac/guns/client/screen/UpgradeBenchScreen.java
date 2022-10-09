package com.tac.guns.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.command.GuiEditor;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.container.UpgradeBenchContainer;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageUpgradeBenchApply;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import com.tac.guns.util.GunEnchantmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class UpgradeBenchScreen extends ContainerScreen<UpgradeBenchContainer>
{
    private static final ResourceLocation GUI_BASE = new ResourceLocation("tac:textures/gui/upgrade_table.png");
    private static final ResourceLocation GUI_PARTS = new ResourceLocation("tac:textures/gui/upgrade_table_parts.png");
    private List<RequirementItem> requirements;
    private PlayerInventory playerInventory;
    private UpgradeBenchTileEntity workbench;
    private String btnSelected;

    private int scrollItor = 0;

    private int lmbdaItor = 0;
    public UpgradeBenchScreen(UpgradeBenchContainer container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.workbench = container.getBench();
        this.xSize = 256;
        this.ySize = 184;
        this.requirements = new ArrayList<>();
    }
    @Override
    public void init()
    {
        super.init();
        GuiEditor.GUI_Element data = new GuiEditor.GUI_Element(0,0,0,0);
        if(GuiEditor.get() != null)
        {
            if(GuiEditor.get().currElement == 2 && GuiEditor.get().GetFromElements(GuiEditor.get().currElement) != null)
                data = GuiEditor.get().GetFromElements(GuiEditor.get().currElement);
        }

        this.addButton(new GuiEnchantmentApplyButton(this.guiLeft + 9+ data.getxMod()+152+74 , this.guiTop + 18 + data.getyMod()+96+50, 23+data.getSizeXMod(), 15+data.getSizeYMod(), 44, 15, button ->
        {
            PacketHandler.getPlayChannel().sendToServer(new MessageUpgradeBenchApply(this.workbench.getPos(), this.btnSelected));
        }));

        GunEnchantmentHelper.upgradeableEnchs.forEach((key, value) ->
        {
            if(this.lmbdaItor == GunEnchantmentHelper.upgradeableEnchs.size())
                return;
            int lmbdaTmp=this.lmbdaItor;
            int scroll = this.scrollItor;
            /*if ((lmbdaTmp-3 > scroll) || lmbdaTmp < scroll)
            {
                return;
            }*/
            if (lmbdaTmp-scroll < 0 || lmbdaTmp-3 > scroll)
            {
                return;
            }
            this.addButton(new GuiEnchantmentOptionButton(this.guiLeft + 9+152+74-170,
                    this.guiTop + 18+96+20-100+((lmbdaTmp+scroll)*34),
                    this.guiLeft + 9+152+74, this.guiTop + 18+96+20,
                    76*2, 16*2,
                    value,
                    3, lmbdaTmp, key
                    , button ->
            {
                this.setSelectedBtn(key);
            }));
            this.lmbdaItor++; // Lambdas stink
        });
    }

    public void setSelectedBtn(String name)
    {
        this.btnSelected = name;
    }

    @Override
    public void tick() // Can the player apply the current module? check recipe along with progression on a weapon
    {
        super.tick();

        /*for(RequirementItem material : this.requirements)
        {
            material.update(this.workbench.getStackInSlot(0),
                    this.workbench.getStackInSlot(1).getCount());
        }*/

        boolean canCraft = true;
        for(RequirementItem material : this.requirements)
        {
            if(!material.isEnabled())
            {
                canCraft = false;
                break;
            }
        }
        this.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        int startX = this.guiLeft;
        int startY = this.guiTop;

        /*if (this.filteredMaterials == null)
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
        }*/
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        /* Fixes partial ticks to use percentage from 0 to 1 */
        partialTicks = Minecraft.getInstance().getRenderPartialTicks();

        int startX = this.guiLeft;
        int startY = this.guiTop;

        RenderSystem.enableBlend();

        //matrixStack.push();
         //3.87
        GuiEditor.GUI_Element data = new GuiEditor.GUI_Element(0,0,0,0);
        if(GuiEditor.get() != null)
        {
            if(GuiEditor.get().currElement == 1 && GuiEditor.get().GetFromElements(GuiEditor.get().currElement) != null)
                data = GuiEditor.get().GetFromElements(GuiEditor.get().currElement);
        }
        this.minecraft.getTextureManager().bindTexture(GUI_BASE);
        //matrixStack.scale(4f, 4f, 0);
        //this.blit(matrixStack, startX, startY, 0, 0, 496, 175);
/*        "xMod": -225.0,
            "yMod": 40.5,
        */
        blit(matrixStack, startX + data.getxMod()-224 + 112, startY+ data.getyMod()+39-30, 496, 175
                , 0,
                0, 496, 175, 1024
                , 1024);
        //this.blit(matrixStack, startX-112, startY-30, 0, 0, 496, 175);
        //matrixStack.pop();

        ItemStack currentItem = this.workbench.getStackInSlot(0);//this.displayStack;
        if(currentItem == null)
            return;


        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(startX + 8 -114, startY + 17 -6, 160, 70);

        IRenderTypeBuffer.Impl buffer = this.minecraft.getRenderTypeBuffers().getBufferSource();
        RenderSystem.pushMatrix();
        {

            RenderSystem.translatef(startX+ 88 -118, startY + 60 -10, 100);
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

            GunRenderingHandler.get().renderWeapon(Minecraft.getInstance().player, currentItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
            //Minecraft.getInstance().getItemRenderer().renderItem(currentItem, ItemCameraTransforms.TransformType.FIXED, false, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, RenderUtil.getModel(currentItem));

            buffer.finish();

            RenderSystem.disableAlphaTest();
            RenderSystem.disableRescaleNormal();
        }
        RenderSystem.popMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        /*this.requirements = this.requirements();
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

                Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(this.font, stack, startX + 172 + 2, startY + i * 19 + 1 + 63, null);

            }
        }*/
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        /*if(keyCode == GLFW.GLFW_KEY_UP) {
            this.scrollItor--;
        }
        else if(keyCode == GLFW.GLFW_KEY_DOWN)
        {
            this.scrollItor++;
        }
        else*/ super.keyPressed(keyCode, scanCode, modifiers);
        //initScrollingButtons();
        /*this.lmbdaItor=0;
        */this.init();
        return true;
    }

    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
    {
        if(scroll < 0 && this.scrollItor > 0)
        {
            this.scrollItor--;
            init();
            //initScrollingButtons();
        }
        else if(scroll > 0)
        {
            this.scrollItor++;
            init();
            //initScrollingButtons();
        }
        return false;
    }*/

    protected void drawText(MatrixStack matrixStack, int x, int y, ITextComponent text)
    {
        int startX = this.guiLeft;
        int startY = this.guiTop;
        this.font.drawString(matrixStack, text.getString(), startX+x, startY+y,
                Color.WHITE.getRGB());
        // What in the hell? Not scalable, how to make this scale on gui scale?

    }

    public class GuiEnchantmentOptionButton extends UpgradeTableButton {
        private RequirementItem requirement;
        private String name;
        public int enchLevel = 0;
        public int maxEnchLevel;
        private int itorInt; // Start from 0, since it will start at list start
        public boolean selected = false;
        public RequirementItem getRequirement(){return this.requirement;}
        public void GuiEnchantmentOptionButton() {
            this.onPress.onPress(this);
        }
        public GuiEnchantmentOptionButton(int x, int y, int u, int v, int widthIn, int heightIn,
                                          RequirementItem requirement, int maxEnchLevel, int itor
                , String name, IPressable onPress)
        {
            super(x, y, u, v, widthIn, heightIn, onPress);
            this.itorInt = itor;
            this.requirement = requirement;
            this.maxEnchLevel = maxEnchLevel;
            this.name = name;

            GunEnchantmentHelper.upgradeableEnchs.put(name, requirement);
        }

        /*@Override
        public void onPress()
        {
            super.onPress();
        }*/

        @Override
        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
        {
            //super.renderButton(matrixStack,mouseX,mouseY,partialTicks);
            Minecraft mc = Minecraft.getInstance();
            if (!visible/* || (this.itorInt-3 > scrollItor)*/)
            {
                return;
            }

            /// EDITOR
            GuiEditor.GUI_Element data = new GuiEditor.GUI_Element(0,0,0,0);

            /// EDITOR

            mc.getTextureManager().bindTexture(GUI_PARTS);
            boolean btnSelectedYet = btnSelected != null;
            if (this.enchLevel == this.maxEnchLevel)
            {
                //matrixStack.scale(2f, 2f, 0); //3.87
                blit(matrixStack, this.u + data.getxMod()+88 - 174-84,
                        this.v + data.getyMod() - 4 - 10 - 50-34+((this.itorInt+scrollItor)*33),
                        152, 32, 0, 112, 152, 32, 512, 512);

            }// MAX
            else if(this.isHovered() || btnSelectedYet && btnSelected.equalsIgnoreCase(this.name))
            {
                //matrixStack.scale(2f, 2f, 0); //3.87
                blit(matrixStack, this.u + data.getxMod()+88 - 174-84,
                        this.v + data.getyMod() - 4 - 10 - 50-34+((this.itorInt+scrollItor)*33),
                        152, 32, 0, 160, 152, 32, 512, 512);
            }
            else
            {
                blit(matrixStack, this.u + data.getxMod()+88 - 174-84,
                        this.v + data.getyMod() - 4 - 10 - 50-34+((this.itorInt+scrollItor)*33),
                        152, 32, 0, 64, 152, 32, 512, 512);
            }
            GuiEditor.GUI_Element data1 = new GuiEditor.GUI_Element(0,0,0,0);
            if(GuiEditor.get() != null)
            {
                if(GuiEditor.get().currElement == 7 && GuiEditor.get().GetFromElements(GuiEditor.get().currElement) != null)
                    data1 = GuiEditor.get().GetFromElements(GuiEditor.get().currElement);
            }
            for(int i = 0; i < this.maxEnchLevel && this.enchLevel != this.maxEnchLevel; i++)
            {
                //Render blue
                if(this.enchLevel > i) {
                    mc.ingameGUI.blit(matrixStack, this.u + data.getxMod()+88 - 174-84 + (i*12),
                            this.v +13+ data.getyMod() - 4 - 10 - 50-21+((this.itorInt+scrollItor)*33), 0, 101,
                            12, 3);
                }
                else {
                    mc.ingameGUI.blit(matrixStack, this.u + data.getxMod()+88 - 174-84 + (i*12),
                            this.v +13+ data.getyMod() - 4 - 10 - 50-21+((this.itorInt+scrollItor)*33), 0, 98,
                            12, 3);
                }
            }
            drawText(matrixStack,77+data1.getxMod(),
                    data1.getyMod()+40+1*(int)((this.itorInt+scrollItor)*33), new TranslationTextComponent(this.name));

        }
    }
    public class GuiEnchantmentApplyButton extends UpgradeTableButton {
        public void GuiEnchantmentApplyButton() {
            this.onPress.onPress(this);
        }
        public GuiEnchantmentApplyButton(int x, int y, int u, int v, int widthIn, int heightIn, IPressable onPress) {super(x, y, u, v, widthIn, heightIn, onPress);}
        @Override
        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
        {
            Minecraft mc = Minecraft.getInstance();
            if (!visible)
            {
                return;
            }
            GuiEditor.GUI_Element data = new GuiEditor.GUI_Element(0,0,0,0);
            mc.getTextureManager().bindTexture(GUI_PARTS);
            if(this.isHovered)
            {
                blit(matrixStack, this.x + data.getxMod()+173 - 74 - 26 - 74,
                        this.y + data.getyMod()+103 - 44 - 10 - 50, 24*2, 0, 23*2, this.height*2,
                        512, 512);
            }
            else
            {
                blit(matrixStack, this.x + data.getxMod()+173 - 74 - 26 - 74,
                        this.y + data.getyMod()+103 - 44 - 10 - 50, 0, 0, 23*2, this.height*2, 512, 512);
            }
        }
    }
    public class UpgradeTableButton extends Button {
        protected final IPressable onPress;
        int u;
        int v;
        int x;
        int y;
        int widthIn;
        int heightIn;

        public void onPress() {
            this.onPress.onPress(this);
        }

        public UpgradeTableButton(int x, int y, int u, int v, int widthIn, int heightIn, IPressable onPress) {

            super(x, y, widthIn, heightIn, new TranslationTextComponent("tac.empt"), onPress);
            this.u = u;
            this.v = v;
            this.x = x;
            this.y = y;
            this.widthIn = widthIn;
            this.heightIn = heightIn;
            this.onPress = onPress;
        }
    }

    /*private static final int[] levelReqPerEnch = new int[]{};
    private List<RequirementItem> getRequirements()
    {
        List<RequirementItem> materials = NonNullList.withSize(6,
                new RequirementItem(this.workbench.getStackInSlot(1).getCount(),
                        levelReqPerEnch));
        //List<MaterialItem> filteredMaterials = this.requirements.stream().filter(materialItem ->
        // this.checkBoxMaterials.isToggled() ? !materialItem.isEnabled() : !materialItem.stack.isEmpty()).collect(Collectors.toList());
        for(int i = 0; i < this.requirements.size() && i < materials.size(); i++)
        {
            materials.set(i, this.requirements.get(i));
        }
        return materials;
    }*/
    public static class RequirementItem {
        public static final RequirementItem EMPTY = new RequirementItem();

        public Enchantment enchantment;
        private boolean enabled = false;

        private int[] moduleCounts = new int[]{};
        private int[] levelReqs = new int[]{};
        public int[] getModuleCount() {return moduleCounts;}
        public int[] getLevelReq() {return levelReqs;}
        public RequirementItem() {}

        public RequirementItem(int[] moduleCounts, int[] levelReqs, Enchantment enchantment)
        {
            this.enchantment = enchantment;
            this.moduleCounts = moduleCounts;
            this.levelReqs = levelReqs;
        }

        public void update(ItemStack stack, int moduleCount, int upgradeLevel)
        {
            if(stack.getTag() != null)
            {
                if(stack.getTag().getInt("level") >= this.levelReqs[upgradeLevel] && this.moduleCounts[upgradeLevel] >= moduleCount)
                    this.enabled = true;
            }
        }

        public boolean isEnabled()
        {
            return this.enabled;
        }
    }
}
