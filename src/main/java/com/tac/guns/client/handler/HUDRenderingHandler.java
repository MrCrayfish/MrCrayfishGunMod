package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.common.Gun;
import com.tac.guns.common.ReloadTracker;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.GunEnchantmentHelper;
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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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

    private static final ResourceLocation[] FIREMODE_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/safety.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/semi.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/full.png")
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

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if (Minecraft.getInstance().player == null || !(Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof GunItem)) {
            return;
        }
        if(!Config.CLIENT.weaponGUI.weaponGui.get()) {
            return;
        }

        ClientPlayerEntity player = Minecraft.getInstance().player;
        ItemStack heldItem = player.getHeldItemMainhand();
        Gun gun = ((TimelessGunItem) heldItem.getItem()).getGun();
        MatrixStack stack = event.getMatrixStack();
        float anchorPointX = event.getWindow().getScaledWidth() / 12F * 11F;
        float anchorPointY = event.getWindow().getScaledHeight() / 10F * 9F;

        float configScaleWeaponType = Config.CLIENT.weaponGUI.weaponTypeIcon.weaponIconSize.get().floatValue();
        float configScaleWeaponCounter = Config.CLIENT.weaponGUI.weaponAmmoCounter.weaponAmmoCounterSize.get().floatValue();
        float configScaleWeaponFireMode = Config.CLIENT.weaponGUI.weaponFireMode.weaponFireModeSize.get().floatValue();
        float configScaleWeaponReloadBar = Config.CLIENT.weaponGUI.weaponReloadTimer.weaponReloadTimerSize.get().floatValue();

        float iconSize = 64.0F * configScaleWeaponType;
        float counterSize = 2.0F * configScaleWeaponCounter;
        float fireModeSize = 32.0F * configScaleWeaponFireMode;
        float ReloadBarSize = 32.0F * configScaleWeaponReloadBar;

        RenderSystem.enableAlphaTest();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        if(Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.get()) {
            // Weapon icon rendering
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            stack.push();
            {
                stack.translate(anchorPointX, anchorPointY, 0);
                stack.translate(-iconSize + (-Config.CLIENT.weaponGUI.weaponTypeIcon.x.get().floatValue()), -iconSize + (-Config.CLIENT.weaponGUI.weaponTypeIcon.y.get().floatValue()), 0);

                if (gun.getDisplay().getWeaponType() > 5 || gun.getDisplay().getWeaponType() < 0)
                    Minecraft.getInstance().getTextureManager().bindTexture(AMMO_ICONS[0]);
                else
                    Minecraft.getInstance().getTextureManager().bindTexture(AMMO_ICONS[gun.getDisplay().getWeaponType()]);

                Matrix4f matrix = stack.getLast().getMatrix();
                buffer.pos(matrix, 0, iconSize, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, iconSize, iconSize, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, iconSize, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            }
            stack.pop();
            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
        }
        if(Config.CLIENT.weaponGUI.weaponFireMode.showWeaponFireMode.get()) {
            // FireMode rendering
            RenderSystem.enableAlphaTest();
            buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            stack.push();
            {
                stack.translate(anchorPointX - (fireModeSize*2) / 4F, anchorPointY - (fireModeSize*2) / 5F * 3F, 0);
                stack.translate(-fireModeSize + (-Config.CLIENT.weaponGUI.weaponFireMode.x.get().floatValue()), -fireModeSize + (-Config.CLIENT.weaponGUI.weaponFireMode.y.get().floatValue()), 0);

                int fireMode;

                if(player.getHeldItemMainhand().getTag() == null)
                    fireMode = gun.getGeneral().getRateSelector()[0];
                else if(player.getHeldItemMainhand().getTag().getInt("CurrentFireMode") == 0)
                    fireMode = gun.getGeneral().getRateSelector()[0];
                else
                    fireMode = Objects.requireNonNull(player.getHeldItemMainhand().getTag()).getInt("CurrentFireMode");

                /*if (fireMode > 2 || fireMode < 0) // Weapons with unsupported modes will render as "default"
                    Minecraft.getInstance().getTextureManager().bindTexture(FIREMODE_ICONS[2]);*/
                if (!Config.COMMON.gameplay.safetyExistence.get() && fireMode == 0)
                    Minecraft.getInstance().getTextureManager().bindTexture(FIREMODE_ICONS[0]); // Render true firemode
                else
                    Minecraft.getInstance().getTextureManager().bindTexture(FIREMODE_ICONS[fireMode]); // Render true firemode

                Matrix4f matrix = stack.getLast().getMatrix();
                buffer.pos(matrix, 0, fireModeSize, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, fireModeSize, fireModeSize, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, fireModeSize, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
            }
            stack.pop();
            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
        }
        if(Config.CLIENT.weaponGUI.weaponReloadTimer.showWeaponReloadTimer.get() && ReloadHandler.get().isReloading())//Replace with reload bar checker
        {
            // FireMode rendering
            RenderSystem.enableAlphaTest();
            buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            stack.push();
            {
                stack.translate(anchorPointX - (ReloadBarSize*4.35) / 4F, anchorPointY + (ReloadBarSize*1.625F) / 5F * 3F, 0);//stack.translate(anchorPointX - (fireModeSize*6) / 4F, anchorPointY - (fireModeSize*1F) / 5F * 3F, 0); // *68for21F
                stack.translate(-ReloadBarSize + (-Config.CLIENT.weaponGUI.weaponReloadTimer.x.get().floatValue()), -ReloadBarSize + (-Config.CLIENT.weaponGUI.weaponReloadTimer.y.get().floatValue()), 0);
               // stack.translate(0, 0, );
                stack.scale(2.1F*(1-ReloadHandler.get().getReloadProgress(event.getPartialTicks(), heldItem)),0.25F,0); // *21F
                Minecraft.getInstance().getTextureManager().bindTexture(RELOAD_ICONS[0]); // Future options to render bar types

                Matrix4f matrix = stack.getLast().getMatrix();
                buffer.pos(matrix, 0, ReloadBarSize, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, ReloadBarSize, ReloadBarSize, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                buffer.pos(matrix, ReloadBarSize, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
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
                    String text = player.getHeldItemMainhand().getTag().getInt("AmmoCount") + " / " + GunEnchantmentHelper.getAmmoCapacity(heldItem, gun);
                    stack.scale(counterSize, counterSize, counterSize);
                    drawCenteredString(stack, Minecraft.getInstance().fontRenderer, text, 0, 0, 0xffffff);
                }
            }
            stack.pop();
        }
    }
}