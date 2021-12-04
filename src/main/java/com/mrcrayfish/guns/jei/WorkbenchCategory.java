package com.mrcrayfish.guns.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.item.IColored;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 */
public class WorkbenchCategory implements IRecipeCategory<WorkbenchRecipe>
{
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "workbench");
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/workbench.png");
    public static final String TITLE_KEY = Reference.MOD_ID + ".category.workbench.title";
    public static final String MATERIALS_KEY = Reference.MOD_ID + ".category.workbench.materials";

    private final IDrawableStatic background;
    private final IDrawableStatic window;
    private final IDrawableStatic inventory;
    private final IDrawableStatic dyeSlot;
    private final IDrawable icon;
    private final String title;
    private final Item[] dyes;

    public WorkbenchCategory(IGuiHelper helper)
    {
        this.background = helper.createBlankDrawable(162, 124);
        this.window = helper.createDrawable(BACKGROUND, 7, 15, 162, 72);
        this.inventory = helper.createDrawable(BACKGROUND, 7, 101, 162, 36);
        this.dyeSlot = helper.createDrawable(BACKGROUND, 7, 101, 18, 18);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.WORKBENCH.get()));
        this.title = I18n.format(TITLE_KEY);
        this.dyes = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof DyeItem).toArray(Item[]::new);
    }

    @Override
    public ResourceLocation getUid()
    {
        return ID;
    }

    @Override
    public Class<? extends WorkbenchRecipe> getRecipeClass()
    {
        return WorkbenchRecipe.class;
    }

    @Override
    public String getTitle()
    {
        return this.title;
    }

    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public IDrawable getIcon()
    {
        return this.icon;
    }

    @Override
    public void setIngredients(WorkbenchRecipe recipe, IIngredients ingredients)
    {
        List<List<ItemStack>> itemInputs = new ArrayList<>();
        ItemStack output = recipe.getItem();
        if(output.getItem() instanceof IColored)
        {
            IColored colored = (IColored) output.getItem();
            if(colored.canColor(output))
            {
                itemInputs.add(Stream.of(this.dyes).map(ItemStack::new).collect(Collectors.toList()));
            }
        }
        recipe.getMaterials().forEach(material -> itemInputs.add(Arrays.asList(material.getMatchingStacks())));
        ingredients.setInputLists(VanillaTypes.ITEM, itemInputs);
        ingredients.setOutput(VanillaTypes.ITEM,recipe.getItem());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, WorkbenchRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        ItemStack output = recipe.getItem();
        int offset = 0;
        if(output.getItem() instanceof IColored)
        {
            IColored colored = (IColored) output.getItem();
            if(colored.canColor(output))
            {
                stacks.init(0, true, 140, 51);
                offset = 1;
            }
        }
        for(int i = 0; i < ingredients.getInputs(VanillaTypes.ITEM).size(); i++)
        {
            stacks.init(offset + i, true, (i % 8) * 18, 87 + (i / 8) * 18);
        }
        stacks.set(ingredients);
    }

    @Override
    public void draw(WorkbenchRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        this.window.draw(matrixStack, 0, 0);
        this.inventory.draw(matrixStack, 0, this.window.getHeight() + 2 + 11 + 2);
        this.dyeSlot.draw(matrixStack, 139, 50);

        Minecraft.getInstance().fontRenderer.drawString(matrixStack, I18n.format(MATERIALS_KEY), 0, 78, 0x7E7E7E);

        ItemStack output = recipe.getItem();
        IFormattableTextComponent displayName = output.getDisplayName().deepCopy();
        if(output.getCount() > 1)
        {
            displayName.append(new StringTextComponent(" x " + output.getCount()).mergeStyle(TextFormatting.GOLD, TextFormatting.BOLD));
        }
        int titleX = this.window.getWidth() / 2;
        AbstractGui.drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, displayName, titleX, 5, Color.WHITE.getRGB());

        RenderSystem.pushMatrix();
        {
            RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
            RenderSystem.translatef(81, 40, 1050);
            RenderSystem.scalef(-1.0F, -1.0F, -1.0F);

            MatrixStack matrixstack = new MatrixStack();
            matrixstack.translate(0.0D, 0.0D, 1000.0D);
            matrixstack.scale(40F, 40F, 40F);
            matrixstack.rotate(Vector3f.XP.rotationDegrees(-5F));
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            matrixstack.rotate(Vector3f.YP.rotationDegrees(Minecraft.getInstance().player.ticksExisted + partialTicks));

            RenderSystem.enableRescaleNormal();
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            IBakedModel model = RenderUtil.getModel(output);
            boolean notSideLit = !model.isSideLit();
            if(notSideLit)
            {
                RenderHelper.setupGuiFlatDiffuseLighting();
            }

            IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
            Minecraft.getInstance().getItemRenderer().renderItem(output, ItemCameraTransforms.TransformType.FIXED, false, matrixstack, buffer, 15728880, OverlayTexture.NO_OVERLAY, model);
            buffer.finish();

            if(notSideLit)
            {
                RenderHelper.setupGui3DDiffuseLighting();
            }

            RenderSystem.disableAlphaTest();
            RenderSystem.disableRescaleNormal();
        }
        RenderSystem.popMatrix();
    }
}
