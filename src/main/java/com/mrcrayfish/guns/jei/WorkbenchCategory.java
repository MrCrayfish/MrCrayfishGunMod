package com.mrcrayfish.guns.jei;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.item.IColored;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 */
public class WorkbenchCategory implements IRecipeCategory<WorkbenchRecipe> {
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "workbench");
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/workbench.png");
    public static final String TITLE_KEY = Reference.MOD_ID + ".category.workbench.title";
    public static final String MATERIALS_KEY = Reference.MOD_ID + ".category.workbench.materials";

    private final IDrawableStatic background;
    private final IDrawableStatic window;
    private final IDrawableStatic inventory;
    private final IDrawableStatic dyeSlot;
    private final IDrawable icon;
    private final Component title;
    private final Item[] dyes;

    public WorkbenchCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(162, 124);
        this.window = helper.createDrawable(BACKGROUND, 7, 15, 162, 72);
        this.inventory = helper.createDrawable(BACKGROUND, 7, 101, 162, 36);
        this.dyeSlot = helper.createDrawable(BACKGROUND, 7, 101, 18, 18);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WORKBENCH.get()));
        this.title = Component.translatable(TITLE_KEY);
        this.dyes = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof DyeItem).toArray(Item[]::new);
    }

    @Override
    public RecipeType<WorkbenchRecipe> getRecipeType() {
        return GunModPlugin.WORKBENCH;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WorkbenchRecipe recipe, IFocusGroup focuses) {
        ItemStack output = recipe.getItem();
        if (IColored.isDyeable(output)) {
            builder.addSlot(RecipeIngredientRole.INPUT, 141, 52).addItemStacks(Stream.of(this.dyes).map(ItemStack::new).collect(Collectors.toList()));
        }
        for (int i = 0; i < recipe.getMaterials().size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, (i % 8) * 18 + 1, 88 + (i / 8) * 18).addIngredients(recipe.getMaterials().get(i));
        }
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(output);
    }
    
    @Override
    public void draw(WorkbenchRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.window.draw(graphics, 0, 0);
        this.inventory.draw(graphics, 0, this.window.getHeight() + 2 + 11 + 2);
        this.dyeSlot.draw(graphics, 140, 51);
        
        graphics.drawString(Minecraft.getInstance().font, I18n.get(MATERIALS_KEY), 0, 78, Color.WHITE.getRGB());

        ItemStack output = recipe.getItem();
        MutableComponent displayName = output.getHoverName().copy();
        if (output.getCount() > 1) {
            displayName.append(Component.literal(" x " + output.getCount()).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        }
        int titleX = this.window.getWidth() / 2;
        graphics.drawCenteredString(Minecraft.getInstance().font, displayName, titleX, 5, Color.WHITE.getRGB());

        PoseStack stack = RenderSystem.getModelViewStack();
        stack.pushPose();
        {
            stack.mulPoseMatrix(graphics.pose().last().pose());
            stack.translate(81, 40, 0);
            stack.scale(40F, 40F, 40F);
            stack.mulPose(Axis.XP.rotationDegrees(-5F));
            float partialTicks = Minecraft.getInstance().getFrameTime();
            stack.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().player.tickCount + partialTicks));
            stack.scale(-1, -1, -1);
            RenderSystem.applyModelViewMatrix();

            BakedModel model = RenderUtil.getModel(output);
            Lighting.setupFor3DItems();

            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            Minecraft.getInstance().getItemRenderer().render(output, ItemDisplayContext.FIXED, false, new PoseStack(), buffer, 15728880, OverlayTexture.NO_OVERLAY, model);
            buffer.endBatch();
        }
        stack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
