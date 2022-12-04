package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.client.util.Easings;
import com.mrcrayfish.guns.common.properties.SightAnimation;
import com.mrcrayfish.guns.item.attachment.impl.Scope;

/**
 * Author: MrCrayfish
 */
public class Attachments
{
    public static final Scope SHORT_SCOPE = Scope.builder().aimFovModifier(0.7F).reticleOffset(1.55F).viewFinderDistance(0.7).viewportFov(30.0).sightAnimation(SightAnimation.builder().setViewportCurve(Easings.LINEAR).setSightCurve(Easings.EASE_OUT_QUAD).setFovCurve(Easings.LINEAR).setAimTransformCurve(Easings.EASE_IN_QUAD)).modifiers(GunModifiers.SLOW_ADS).build();
    public static final Scope MEDIUM_SCOPE = Scope.builder().aimFovModifier(0.5F).reticleOffset(1.625F).viewFinderDistance(1.0).viewportFov(20.0).sightAnimation(SightAnimation.builder().setViewportCurve(Easings.EASE_OUT_CUBIC).setSightCurve(Easings.EASE_OUT_QUAD).setFovCurve(Easings.EASE_IN_OUT_QUAD).setAimTransformCurve(Easings.EASE_IN_QUAD)).modifiers(GunModifiers.SLOW_ADS).build();
    public static final Scope LONG_SCOPE = Scope.builder().aimFovModifier(0.25F).reticleOffset(1.4F).viewFinderDistance(1.4).viewportFov(10.0).sightAnimation(SightAnimation.builder().setViewportCurve(Easings.EASE_OUT_CUBIC).setSightCurve(Easings.EASE_OUT_QUAD).setFovCurve(Easings.EASE_OUT_QUAD).setAimTransformCurve(Easings.EASE_IN_QUAD)).modifiers(GunModifiers.SLOWER_ADS).build();
}
