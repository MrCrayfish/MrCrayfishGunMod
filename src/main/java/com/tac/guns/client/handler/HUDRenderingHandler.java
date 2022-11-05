package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.command.ObjectRenderEditor;
import com.tac.guns.common.Gun;
import com.tac.guns.common.ReloadTracker;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class HUDRenderingHandler extends AbstractGui {
    private static HUDRenderingHandler instance;

    private static final ResourceLocation[] AMMO_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/counterassule_rifle.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/counterlmg.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/counterpistol.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/countershotgun.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/countersmg.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/countersniper.png")
            };

    private static final ResourceLocation[] FIREMODE_ICONS_OLD = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/safety.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/semi.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/full.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/burst.png"),
            };
    private static final ResourceLocation[] FIREMODE_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_safety.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_semi.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_auto.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_burst.png"),
            };
    private static final ResourceLocation[] RELOAD_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/reloadbar.png")
            };

    public static HUDRenderingHandler get() {
        return instance == null ? instance = new HUDRenderingHandler() : instance;
    }

    private HUDRenderingHandler() {
    }

    private int ammoReserveCount = 0;
    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END)
            return;
        if(Minecraft.getInstance().player == null)
            return;
        if(Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof GunItem)
        {
            GunItem gunItem = (GunItem) Minecraft.getInstance().player.getHeldItemMainhand().getItem();
            this.ammoReserveCount = ReloadTracker.calcMaxReserveAmmo(Gun.findAmmo(Minecraft.getInstance().player, gunItem.getGun().getProjectile().getItem()));
        }
    }
    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if (Minecraft.getInstance().player == null || !(Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof TimelessGunItem)) {
            return;
        }
        if(!Config.CLIENT.weaponGUI.weaponGui.get()) {
            return;
        }

        ClientPlayerEntity player = Minecraft.getInstance().player;
        ItemStack heldItem = player.getHeldItemMainhand();
        TimelessGunItem gunItem = (TimelessGunItem) heldItem.getItem();
        Gun gun = gunItem.getGun();
        MatrixStack stack = event.getMatrixStack();
        float anchorPointX = event.getWindow().getScaledWidth() / 12F * 11F;
        float anchorPointY = event.getWindow().getScaledHeight() / 10F * 9F;

        float configScaleWeaponCounter = Config.CLIENT.weaponGUI.weaponAmmoCounter.weaponAmmoCounterSize.get().floatValue();
        float configScaleWeaponFireMode = Config.CLIENT.weaponGUI.weaponFireMode.weaponFireModeSize.get().floatValue();
        float configScaleWeaponReloadBar = Config.CLIENT.weaponGUI.weaponReloadTimer.weaponReloadTimerSize.get().floatValue();

        float counterSize = 1.8F * configScaleWeaponCounter;
        float fireModeSize = 32.0F * configScaleWeaponFireMode;
        float ReloadBarSize = 32.0F * configScaleWeaponReloadBar;

        RenderSystem.enableAlphaTest();
        BufferBuilder buffer;
        if(Config.CLIENT.weaponGUI.weaponFireMode.showWeaponFireMode.get()) {
            // FireMode rendering
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.defaultAlphaFunc();
            buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            stack.push();
            {
                stack.translate(anchorPointX - (fireModeSize*2) / 4F, anchorPointY - (fireModeSize*2) / 5F * 3F, 0);
                stack.translate(-fireModeSize + (-62.7) + (-Config.CLIENT.weaponGUI.weaponFireMode.x.get().floatValue()), -fireModeSize + 52.98 + (-Config.CLIENT.weaponGUI.weaponFireMode.y.get().floatValue()), 0);

                stack.translate(20, 5, 0);
                int fireMode;

                if(player.getHeldItemMainhand().getTag() == null)
                    fireMode = gun.getGeneral().getRateSelector()[0];
                else if(player.getHeldItemMainhand().getTag().getInt("CurrentFireMode") == 0)
                    fireMode = gun.getGeneral().getRateSelector()[0];
                else
                    fireMode = Objects.requireNonNull(player.getHeldItemMainhand().getTag()).getInt("CurrentFireMode");
                //int fireMode = gunItem.getSupportedFireModes()[gunItem.getCurrFireMode()];
                if (!Config.COMMON.gameplay.safetyExistence.get() && fireMode == 0)
                    Minecraft.getInstance().getTextureManager().bindTexture(FIREMODE_ICONS[0]); // Render true firemode
                else
                    Minecraft.getInstance().getTextureManager().bindTexture(FIREMODE_ICONS[fireMode]); // Render true firemode

                Matrix4f matrix = stack.getLast().getMatrix();
                buffer.pos(matrix, 0, fireModeSize/2, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, fireModeSize/2, fireModeSize/2, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, fireModeSize/2, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            }
            stack.pop();
            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
        }
        if(Config.CLIENT.weaponGUI.weaponAmmoCounter.showWeaponAmmoCounter.get()) {
            // Text rendering
            stack.push();
            {
                stack.translate(
                    (anchorPointX - (counterSize*32) / 2) + (-Config.CLIENT.weaponGUI.weaponAmmoCounter.x.get().floatValue()),
                    (anchorPointY - (counterSize*32) / 4) + (-Config.CLIENT.weaponGUI.weaponAmmoCounter.y.get().floatValue()),
                    0
            );
            if(player.getHeldItemMainhand().getTag() != null) {
                IFormattableTextComponent currentAmmo;
                IFormattableTextComponent reserveAmmo = new TranslationTextComponent("");
                int ammo = player.getHeldItemMainhand().getTag().getInt("AmmoCount");
                if (player.getHeldItemMainhand().getTag().getInt("AmmoCount") <= gun.getReloads().getMaxAmmo() / 4 && this.ammoReserveCount <= gun.getReloads().getMaxAmmo()) {
                    currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo)).mergeStyle(TextFormatting.RED);
                    reserveAmmo =
                            byPaddingZeros(this.ammoReserveCount > 10000 ? 10000 : this.ammoReserveCount).append(new TranslationTextComponent("" + (this.ammoReserveCount > 10000 ? 9999 : this.ammoReserveCount))).mergeStyle(TextFormatting.RED);
                } else if (this.ammoReserveCount <= gun.getReloads().getMaxAmmo()) {
                    currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo).mergeStyle(TextFormatting.WHITE));
                    reserveAmmo = byPaddingZeros(this.ammoReserveCount > 10000 ? 10000 : this.ammoReserveCount).append(new TranslationTextComponent("" + (this.ammoReserveCount > 10000 ? 9999 : this.ammoReserveCount))).mergeStyle(TextFormatting.RED);
                } else if (player.getHeldItemMainhand().getTag().getInt("AmmoCount") <= gun.getReloads().getMaxAmmo() / 4) {
                    currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo)).mergeStyle(TextFormatting.RED);
                    reserveAmmo = byPaddingZeros(this.ammoReserveCount > 10000 ? 10000 : this.ammoReserveCount).append(new TranslationTextComponent("" + (this.ammoReserveCount > 10000 ? 9999 : this.ammoReserveCount))).mergeStyle(TextFormatting.GRAY);
                } else {
                    currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo).mergeStyle(TextFormatting.WHITE));
                    reserveAmmo = byPaddingZeros(this.ammoReserveCount > 10000 ? 10000 : this.ammoReserveCount).append(new TranslationTextComponent("" + (this.ammoReserveCount > 10000 ? 9999 : this.ammoReserveCount))).mergeStyle(TextFormatting.GRAY);
                }
                stack.scale(counterSize, counterSize, counterSize);
                stack.push();
                {
                    stack.translate(-21.15, 0, 0 );
                    drawString(stack, Minecraft.getInstance().fontRenderer, currentAmmo, 0, 0, 0xffffff); // Gun ammo
                }
                stack.pop();

                stack.push();
                {
                    stack.scale(0.7f, 0.7f, 0.7f);
                    stack.translate(
                            (3.7),
                            (3.4),
                            0 );
                    drawString(stack, Minecraft.getInstance().fontRenderer, reserveAmmo, 0, 0, 0xffffff); // Reserve ammo
                }
                stack.pop();
                }
            }
            stack.pop();

            stack.push();
            RenderSystem.enableAlphaTest();
            buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            stack.translate(anchorPointX - (ReloadBarSize*4.35) / 4F, anchorPointY + (ReloadBarSize*1.625F) / 5F * 3F, 0);//stack.translate(anchorPointX - (fireModeSize*6) / 4F, anchorPointY - (fireModeSize*1F) / 5F * 3F, 0); // *68for21F
            stack.translate(-ReloadBarSize, -ReloadBarSize, 0);

            stack.translate(-16.25-7.3, 0.15+1.6, 0);
            // stack.translate(0, 0, );
            stack.scale(3.05F,0.028F,0); // *21F
            Minecraft.getInstance().getTextureManager().bindTexture(RELOAD_ICONS[0]); // Future options to render bar types

            Matrix4f matrix = stack.getLast().getMatrix();
            buffer.pos(matrix, 0, ReloadBarSize, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            buffer.pos(matrix, ReloadBarSize, ReloadBarSize, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            buffer.pos(matrix, ReloadBarSize, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();

            stack.translate(19.25, (1.5+(-63.4))*10, 0);
            // stack.translate(0, 0, );
            stack.scale(0.0095F,20.028F,0); // *21F

            buffer.pos(matrix, 0, ReloadBarSize, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            buffer.pos(matrix, ReloadBarSize, ReloadBarSize, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            buffer.pos(matrix, ReloadBarSize, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();

            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
            stack.pop();
        }
    }

    private static IFormattableTextComponent byPaddingZeros(int number) {
        String text = String.format("%0" + (byPaddingZerosCount(number)+1) + "d", 1);
        text = text.substring(0, text.length()-1);
        return new TranslationTextComponent(text).mergeStyle(TextFormatting.GRAY);
    }
    private static int byPaddingZerosCount(int length) {
        if(length < 10)
            return 2;
        if(length < 100)
            return 1;
        if(length < 1000)
            return 0;
        return 0;
    }
}