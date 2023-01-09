package com.mrcrayfish.guns.common.properties;

import com.google.gson.JsonObject;
import com.mrcrayfish.guns.annotation.Optional;
import com.mrcrayfish.guns.client.util.Easings;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.debug.Debug;
import com.mrcrayfish.guns.debug.IDebugWidget;
import com.mrcrayfish.guns.debug.IEditorMenu;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugEnum;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugToggle;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class SightAnimation implements INBTSerializable<CompoundTag>, IEditorMenu
{
    public static final SightAnimation DEFAULT = new SightAnimation();

    @Optional
    protected Easings viewportCurve = Easings.LINEAR;

    @Optional
    protected Easings sightCurve = Easings.EASE_OUT_QUAD;

    @Optional
    protected Easings fovCurve = Easings.LINEAR;

    @Optional
    protected Easings aimTransformCurve = Easings.EASE_IN_QUAD;

    public Easings getViewportCurve()
    {
        return this.viewportCurve;
    }

    public Easings getSightCurve()
    {
        return this.sightCurve;
    }

    public Easings getFovCurve()
    {
        return this.fovCurve;
    }

    public Easings getAimTransformCurve()
    {
        return this.aimTransformCurve;
    }

    /**
     * Internal only
     */
    @Override
    public Component getEditorLabel()
    {
        return new TextComponent("Sight Animation");
    }

    /**
     * Internal only
     */
    @Override
    public void getEditorWidgets(List<Pair<Component, Supplier<IDebugWidget>>> widgets)
    {
        widgets.add(Pair.of(new TextComponent("Debug: ").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD).append(new TextComponent("Force Aim").withStyle(ChatFormatting.WHITE)), () -> new DebugToggle(Debug.isForceAim(), Debug::setForceAim)));
        widgets.add(Pair.of(new TextComponent("Viewport Curve"), () -> new DebugEnum<>(Easings.class, this.viewportCurve, value -> {
            this.viewportCurve = value;
        })));
        widgets.add(Pair.of(new TextComponent("Sight Curve"), () -> new DebugEnum<>(Easings.class, this.sightCurve, value -> {
            this.sightCurve = value;
        })));
        widgets.add(Pair.of(new TextComponent("FOV Curve"), () -> new DebugEnum<>(Easings.class, this.fovCurve, value -> {
            this.fovCurve = value;
        })));
        widgets.add(Pair.of(new TextComponent("Aim Transform Curve"), () -> new DebugEnum<>(Easings.class, this.aimTransformCurve, value -> {
            this.aimTransformCurve = value;
        })));
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putString("ViewportCurve", this.viewportCurve.name().toLowerCase(Locale.ROOT));
        tag.putString("SightCurve", this.sightCurve.name().toLowerCase(Locale.ROOT));
        tag.putString("FovCurve", this.fovCurve.name().toLowerCase(Locale.ROOT));
        tag.putString("AimTransformCurve", this.aimTransformCurve.name().toLowerCase(Locale.ROOT));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag)
    {
        if(tag.contains("ViewportCurve", Tag.TAG_STRING))
        {
            this.viewportCurve = Easings.byName(tag.getString("ViewportCurve"));
        }
        if(tag.contains("SightCurve", Tag.TAG_STRING))
        {
            this.sightCurve = Easings.byName(tag.getString("SightCurve"));
        }
        if(tag.contains("FovCurve", Tag.TAG_STRING))
        {
            this.fovCurve = Easings.byName(tag.getString("FovCurve"));
        }
        if(tag.contains("AimTransformCurve", Tag.TAG_STRING))
        {
            this.aimTransformCurve = Easings.byName(tag.getString("AimTransformCurve"));
        }
    }

    public JsonObject toJsonObject()
    {
        JsonObject object = new JsonObject();
        if(this.viewportCurve != Easings.LINEAR)
        {
            object.addProperty("viewportCurve", this.viewportCurve.getName());
        }
        if(this.sightCurve != Easings.EASE_OUT_QUAD)
        {
            object.addProperty("sightCurve", this.sightCurve.getName());
        }
        if(this.fovCurve != Easings.LINEAR)
        {
            object.addProperty("fovCurve", this.fovCurve.getName());
        }
        if(this.aimTransformCurve != Easings.EASE_IN_QUAD)
        {
            object.addProperty("aimTransformCurve", this.aimTransformCurve.getName());
        }
        return object;
    }

    public SightAnimation copy()
    {
        SightAnimation sightAnimation = new SightAnimation();
        sightAnimation.viewportCurve = this.viewportCurve;
        sightAnimation.sightCurve = this.sightCurve;
        sightAnimation.fovCurve = this.fovCurve;
        sightAnimation.aimTransformCurve = this.aimTransformCurve;
        return sightAnimation;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private final SightAnimation sightAnimation;

        protected Builder()
        {
            this.sightAnimation = new SightAnimation();
        }

        public Builder setViewportCurve(Easings viewportCurve)
        {
            this.sightAnimation.viewportCurve = viewportCurve;
            return this;
        }

        public Builder setSightCurve(Easings sightCurve)
        {
            this.sightAnimation.sightCurve = sightCurve;
            return this;
        }

        public Builder setFovCurve(Easings fovCurve)
        {
            this.sightAnimation.fovCurve = fovCurve;
            return this;
        }

        public Builder setAimTransformCurve(Easings aimTransformCurve)
        {
            this.sightAnimation.aimTransformCurve = aimTransformCurve;
            return this;
        }

        public SightAnimation build()
        {
            return this.sightAnimation.copy();
        }
    }
}
