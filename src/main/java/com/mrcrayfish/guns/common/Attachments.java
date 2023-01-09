package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.client.util.Easings;
import com.mrcrayfish.guns.common.properties.SightAnimation;
import com.mrcrayfish.guns.item.attachment.impl.Scope;

/**
 * Author: MrCrayfish
 */
public class Attachments
{
    public static final Scope SHORT_SCOPE = Scope.builder().additionalZoom(0.1F).centerOffset(1.55F).viewFinderOffset(1.0).viewportFov(20.0).modifiers(GunModifiers.SLOW_ADS).build();
    public static final Scope MEDIUM_SCOPE = Scope.builder().additionalZoom(0.25F).centerOffset(1.625F).viewFinderOffset(2.0).viewportFov(10.0).modifiers(GunModifiers.SLOW_ADS).build();
    public static final Scope LONG_SCOPE = Scope.builder().additionalZoom(0.35F).centerOffset(1.4F).viewFinderOffset(1.8).viewportFov(10.0).sightAnimation(SightAnimation.builder().setViewportCurve(Easings.EASE_OUT_CUBIC).setSightCurve(Easings.EASE_OUT_QUAD).setFovCurve(Easings.EASE_OUT_QUAD).setAimTransformCurve(Easings.EASE_IN_QUAD)).modifiers(GunModifiers.SLOWER_ADS).build();
}
