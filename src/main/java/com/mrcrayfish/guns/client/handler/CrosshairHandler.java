package com.mrcrayfish.guns.client.handler;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.render.crosshair.Crosshair;
import com.mrcrayfish.guns.client.render.crosshair.TechCrosshair;
import com.mrcrayfish.guns.client.render.crosshair.TexturedCrosshair;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class CrosshairHandler
{
    private static CrosshairHandler instance;

    public static CrosshairHandler get()
    {
        if(instance == null)
        {
            instance = new CrosshairHandler();
        }
        return instance;
    }

    private final Map<ResourceLocation, Crosshair> idToCrosshair = new HashMap<>();
    private final List<Crosshair> registeredCrosshairs = new ArrayList<>();
    private Crosshair currentCrosshair = null;

    private CrosshairHandler()
    {
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "better_default")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "circle")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "filled_circle"), false));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "square")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "round")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "arrow")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "dot")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "box")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "hit_marker")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "line")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "t")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "smiley")));
        this.register(new TechCrosshair());
    }

    /**
     * Registers a new crosshair. If the crosshair has already been registered, it will be ignored.
     */
    public void register(Crosshair crosshair)
    {
        if(!this.idToCrosshair.containsKey(crosshair.getLocation()))
        {
            this.idToCrosshair.put(crosshair.getLocation(), crosshair);
            this.registeredCrosshairs.add(crosshair);
        }
    }

    /**
     * Sets the crosshair using the given id. The crosshair with the associated id must be registered
     * or the default crosshair will be used.
     *
     * @param id the id of the crosshair
     */
    public void setCrosshair(ResourceLocation id)
    {
        this.currentCrosshair = this.idToCrosshair.getOrDefault(id, Crosshair.DEFAULT);
    }

    /**
     * Gets the current crosshair
     */
    @Nullable
    public Crosshair getCurrentCrosshair()
    {
        if(this.currentCrosshair == null && this.registeredCrosshairs.size() > 0)
        {
            ResourceLocation id = ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
            this.currentCrosshair = id != null ? this.idToCrosshair.getOrDefault(id, Crosshair.DEFAULT) : Crosshair.DEFAULT;
        }
        return this.currentCrosshair;
    }

    /**
     * Gets a list of registered crosshairs. Please note that this list is immutable.
     */
    public List<Crosshair> getRegisteredCrosshairs()
    {
        return ImmutableList.copyOf(this.registeredCrosshairs);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Pre event)
    {
        if(event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type())
            return;

        Crosshair crosshair = this.getCurrentCrosshair();
        if(AimingHandler.get().getNormalisedAdsProgress() > 0.5)
        {
            event.setCanceled(true);
            return;
        }

        if(crosshair == null || crosshair.isDefault())
        {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        ItemStack heldItem = mc.player.getMainHandItem();
        if(!(heldItem.getItem() instanceof GunItem))
            return;

        event.setCanceled(true);

        if(!mc.options.getCameraType().isFirstPerson())
            return;

        if(mc.player.getUseItem().getItem() == Items.SHIELD)
            return;

        PoseStack stack = event.getGuiGraphics().pose();
        stack.pushPose();
        int scaledWidth = event.getWindow().getGuiScaledWidth();
        int scaledHeight = event.getWindow().getGuiScaledHeight();
        crosshair.render(mc, stack, scaledWidth, scaledHeight, event.getPartialTick());
        stack.popPose();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        Crosshair crosshair = this.getCurrentCrosshair();
        if(crosshair == null || crosshair.isDefault())
            return;

        crosshair.tick();
    }

    @SubscribeEvent
    public void onGunFired(GunFireEvent.Post event)
    {
        Crosshair crosshair = this.getCurrentCrosshair();
        if(crosshair == null || crosshair.isDefault())
            return;

        crosshair.onGunFired();
    }

    /* Updates the crosshair if the config is reloaded. */
    public static void onConfigReload(ModConfigEvent.Reloading event)
    {
        ModConfig config = event.getConfig();
        if(config.getType() == ModConfig.Type.CLIENT && config.getModId().equals(Reference.MOD_ID))
        {
            ResourceLocation id = ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
            if(id != null)
            {
                CrosshairHandler.get().setCrosshair(id);
            }
        }
    }
}
