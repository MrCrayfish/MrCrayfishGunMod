package com.mrcrayfish.guns.debug.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.debug.IDebugWidget;
import com.mrcrayfish.guns.debug.IEditorMenu;
import com.mrcrayfish.guns.util.ScreenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class EditorScreen extends Screen
{
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/debug.png");
    private static final int WIDTH = 150;

    private final Screen parent;
    private final IEditorMenu menu;
    private PropertyList list;
    private int windowWidth, windowHeight;
    private int windowLeft, windowTop;

    public EditorScreen(Screen parent, IEditorMenu menu)
    {
        super(menu.getEditorLabel());
        this.parent = parent;
        this.menu = menu;
    }

    @Override
    protected void init()
    {
        List<Pair<Component, Supplier<IDebugWidget>>> widgets = new ArrayList<>();
        this.menu.getEditorWidgets(widgets);

        this.windowWidth = WIDTH;
        this.windowHeight = widgets.size() * PropertyList.ITEM_HEIGHT + 20 + 10; // 20 is the header
        this.windowLeft = 10;
        this.windowTop = (this.height - this.windowHeight) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> {
            Minecraft.getInstance().setScreen(this.parent);
        }).pos(this.windowLeft + WIDTH - 12 - 4, this.windowTop + 4).size(12, 12).build());

        this.list = new PropertyList();
        this.list.setRenderBackground(false);
        this.list.setRenderTopAndBottom(false);
        this.list.setLeftPos(this.windowLeft + 10);
        this.addWidget(this.list);

        widgets.forEach(pair -> {
            if(pair.getRight().get() instanceof AbstractWidget widget) {
                this.list.addEntry(new PropertyEntry(pair.getLeft(), widget));
            }
        });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        this.drawHeader(this.windowLeft, this.windowTop, this.windowWidth);
        this.drawBody(this.windowLeft + 4, this.windowTop + 20, this.windowWidth - 8, this.windowHeight - 20); // Remove header height
        this.list.render(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.font, this.getTitle(), this.windowLeft + 5, this.windowTop + 6, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    private void drawHeader(int x, int y, int width)
    {
        int height = 20;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        this.drawTexturedRect(x, y, 0, 0, 2, 2, 2, 2);                           /* Top left corner */
        this.drawTexturedRect(x + width - 2, y, 3, 0, 2, 2, 2, 2);               /* Top right corner */
        this.drawTexturedRect(x, y + height - 2, 0, 18, 2, 2, 2, 2);             /* Bottom left corner */
        this.drawTexturedRect(x + width - 2, y + height - 2, 3, 18, 2, 2, 2, 2); /* Bottom right corner */
        this.drawTexturedRect(x + 2, y, 2, 0, width - 4, 2, 1, 2);               /* Top border */
        this.drawTexturedRect(x + 2, y + height - 2, 2, 18, width - 4, 2, 1, 2); /* Bottom border */
        this.drawTexturedRect(x, y + 2, 0, 2, 2, height - 4, 2, 16);             /* Left border */
        this.drawTexturedRect(x + width - 2, y + 2, 3, 2, 2, height - 4, 2, 16); /* Right border */
        this.drawTexturedRect(x + 2, y + 2, 2, 2, width - 4, height - 4, 1, 16); /* Center */
    }

    private void drawBody(int x, int y, int width, int height)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        this.drawTexturedRect(x, y + height - 2, 5, 3, 2, 2, 2, 2);             /* Bottom left corner */
        this.drawTexturedRect(x + width - 2, y + height - 2, 8, 3, 2, 2, 2, 2); /* Bottom right corner */
        this.drawTexturedRect(x + 2, y + height - 2, 7, 3, width - 4, 2, 1, 2); /* Bottom border */
        this.drawTexturedRect(x, y, 5, 2, 2, height - 2, 2, 1);                 /* Left border */
        this.drawTexturedRect(x + width - 2, y, 8, 2, 2, height - 2, 2, 1);     /* Right border */
        this.drawTexturedRect(x + 2, y, 7, 2, width - 4, height - 2, 1, 1);     /* Center */
    }

    private void drawTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight)
    {
        float uScale = 1.0F / 256.0F;
        float vScale = 1.0F / 256.0F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(x, y + height, 0).uv(u * uScale, (v + textureHeight) * vScale).endVertex();
        buffer.vertex(x + width, y + height, 0).uv((u + textureWidth) * uScale, (v + textureHeight) * vScale).endVertex();
        buffer.vertex(x + width, y, 0).uv((u + textureWidth) * uScale, v * vScale).endVertex();
        buffer.vertex(x, y, 0).uv(u * uScale, v * vScale).endVertex();
        BufferUploader.drawWithShader(buffer.end());
    }

    private class PropertyList extends ContainerObjectSelectionList<PropertyEntry>
    {
        public static final int ITEM_HEIGHT = 34;

        public PropertyList()
        {
            super(EditorScreen.this.minecraft, EditorScreen.this.windowWidth - 20, EditorScreen.this.windowHeight, EditorScreen.this.windowTop + 20, EditorScreen.this.windowTop + EditorScreen.this.windowHeight - 5, 34);
        }

        @Override
        public int addEntry(PropertyEntry entry)
        {
            return super.addEntry(entry);
        }

        @Override
        public void updateNarration(NarrationElementOutput output) {}

        @Override
        protected int getScrollbarPosition()
        {
            return this.getLeft() + this.width - 6;
        }

        @Override
        public int getRowLeft()
        {
            return super.getRowLeft() - 2;
        }

        @Override
        public int getRowWidth()
        {
            return EditorScreen.this.windowWidth - 20 - 2 - (this.getMaxScroll() > 0 ? 6 : 0);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
        {
            ScreenUtil.startScissor(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
            super.render(graphics, mouseX, mouseY, partialTick);
            ScreenUtil.endScissor();
        }
    }

    private class PropertyEntry extends ContainerObjectSelectionList.Entry<PropertyEntry>
    {
        private final Component label;
        private final AbstractWidget widget;

        public PropertyEntry(Component label, AbstractWidget widget)
        {
            this.label = label;
            this.widget = widget;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks)
        {
            graphics.drawString(EditorScreen.this.getMinecraft().font, this.label, left + 5, top, 0xFFFFFF);
            this.widget.setX(left);
            this.widget.setY(top + 10);
            this.widget.setWidth(rowWidth);
            this.widget.render(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public List<? extends NarratableEntry> narratables()
        {
            return List.of();
        }

        @Override
        public List<? extends GuiEventListener> children()
        {
            return List.of(this.widget);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY)
        {
            return ScreenUtil.isMouseWithin(EditorScreen.this.list.getRowLeft(), EditorScreen.this.list.getTop(), EditorScreen.this.list.getRowWidth(), EditorScreen.this.list.getHeight(), (int) mouseX, (int) mouseY) && super.isMouseOver(mouseX, mouseY);
        }
    }
}
