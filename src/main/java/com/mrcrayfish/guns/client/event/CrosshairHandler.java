package com.mrcrayfish.guns.client.event;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.Crosshair;
import com.mrcrayfish.guns.client.TexturedCrosshair;
import com.mrcrayfish.guns.hook.GunFireEvent;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
            ResourceLocation id = GunMod.getOptions().getCrosshair();
            this.currentCrosshair = this.idToCrosshair.getOrDefault(id, Crosshair.DEFAULT);
        }
        return this.currentCrosshair;
    }

    /**
     * Gets a list of registered crosshairs. Please note that this list is immutable.
     */
    public ImmutableList<Crosshair> getRegisteredCrosshairs()
    {
        return ImmutableList.copyOf(this.registeredCrosshairs);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        if(event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        Crosshair crosshair = this.getCurrentCrosshair();
        if(crosshair == null || crosshair.isDefault())
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        ItemStack heldItem = mc.player.getHeldItemMainhand();
        if(!(heldItem.getItem() instanceof GunItem))
            return;

        event.setCanceled(true);

        MatrixStack stack = event.getMatrixStack();
        stack.push();
        int scaledWidth = event.getWindow().getScaledWidth();
        int scaledHeight = event.getWindow().getScaledHeight();
        crosshair.render(mc, stack, scaledWidth, scaledHeight, event.getPartialTicks());
        stack.pop();
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
}
