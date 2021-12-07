package com.mrcrayfish.guns.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
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
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
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
    private final Component title;
    private final Item[] dyes;

    public WorkbenchCategory(IGuiHelper helper)
    {
        this.background = helper.createBlankDrawable(162, 124);
        this.window = helper.createDrawable(BACKGROUND, 7, 15, 162, 72);
        this.inventory = helper.createDrawable(BACKGROUND, 7, 101, 162, 36);
        this.dyeSlot = helper.createDrawable(BACKGROUND, 7, 101, 18, 18);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.WORKBENCH.get()));
        this.title = new TranslatableComponent(TITLE_KEY);
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
    public Component getTitle()
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
        recipe.getMaterials().forEach(material ->
        {
            itemInputs.add(Arrays.stream(material.getItems()).map(stack -> {
                ItemStack copy = stack.copy();
                copy.setCount(material.getCount());
                return copy;
            }).collect(Collectors.toList()));
        });
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
    public void draw(WorkbenchRecipe recipe, PoseStack poseStack, double mouseX, double mouseY)
    {
        this.window.draw(poseStack, 0, 0);
        this.inventory.draw(poseStack, 0, this.window.getHeight() + 2 + 11 + 2);
        this.dyeSlot.draw(poseStack, 140, 51);

        Minecraft.getInstance().font.draw(poseStack, I18n.get(MATERIALS_KEY), 0, 78, 0x7E7E7E);

        ItemStack output = recipe.getItem();
        MutableComponent displayName = output.getHoverName().copy();
        if(output.getCount() > 1)
        {
            displayName.append(new TextComponent(" x " + output.getCount()).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        }
        int titleX = this.window.getWidth() / 2;
        GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, displayName, titleX, 5, Color.WHITE.getRGB());

        PoseStack stack = RenderSystem.getModelViewStack();
        stack.pushPose();
        {
            stack.mulPoseMatrix(poseStack.last().pose());
            stack.translate(81, 40, 0);
            stack.scale(40F, 40F, 40F);
            stack.mulPose(Vector3f.XP.rotationDegrees(-5F));
            float partialTicks = Minecraft.getInstance().getFrameTime();
            stack.mulPose(Vector3f.YP.rotationDegrees(Minecraft.getInstance().player.tickCount + partialTicks));
            stack.scale(-1, -1, -1);
            RenderSystem.applyModelViewMatrix();

            BakedModel model = RenderUtil.getModel(output);
            Lighting.setupFor3DItems();

            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            Minecraft.getInstance().getItemRenderer().render(output, ItemTransforms.TransformType.FIXED, false, new PoseStack(), buffer, 15728880, OverlayTexture.NO_OVERLAY, model);
            buffer.endBatch();
        }
        stack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
