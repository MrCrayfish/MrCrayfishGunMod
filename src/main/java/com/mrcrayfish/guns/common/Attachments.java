package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.item.attachment.impl.Scope;

/**
 * Author: MrCrayfish
 */
public class Attachments
{
    public static final Scope SHORT_SCOPE = Scope.builder().additionalZoom(0.1F).centerOffset(1.55F).viewFinderOffset(0.75).modifiers(GunModifiers.SLOW_ADS).build();
    public static final Scope MEDIUM_SCOPE = Scope.builder().additionalZoom(0.25F).centerOffset(1.625F).viewFinderOffset(0.3).modifiers(GunModifiers.SLOW_ADS).build();
    public static final Scope LONG_SCOPE = Scope.builder().additionalZoom(0.4F).centerOffset(1.4F).viewFinderOffset(0.275).modifiers(GunModifiers.SLOWER_ADS).build();
}
