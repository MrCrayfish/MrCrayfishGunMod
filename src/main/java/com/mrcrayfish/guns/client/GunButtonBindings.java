package com.mrcrayfish.guns.client;

import com.mrcrayfish.controllable.client.binding.BindingRegistry;
import com.mrcrayfish.controllable.client.binding.ButtonBinding;
import com.mrcrayfish.controllable.client.input.Buttons;

/**
 * Author: MrCrayfish
 */
public class GunButtonBindings
{
    public static final ButtonBinding SHOOT = new ButtonBinding(Buttons.RIGHT_TRIGGER, "cgm.button.shoot", "button.categories.cgm", GunConflictContext.IN_GAME_HOLDING_WEAPON);
    public static final ButtonBinding AIM = new ButtonBinding(Buttons.LEFT_TRIGGER, "cgm.button.aim", "button.categories.cgm", GunConflictContext.IN_GAME_HOLDING_WEAPON);
    public static final ButtonBinding RELOAD = new ButtonBinding(Buttons.X, "cgm.button.reload", "button.categories.cgm", GunConflictContext.IN_GAME_HOLDING_WEAPON);
    public static final ButtonBinding OPEN_ATTACHMENTS = new ButtonBinding(Buttons.B, "cgm.button.attachments", "button.categories.cgm", GunConflictContext.IN_GAME_HOLDING_WEAPON);
    public static final ButtonBinding STEADY_AIM = new ButtonBinding(Buttons.RIGHT_THUMB_STICK, "cgm.button.steadyAim", "button.categories.cgm", GunConflictContext.IN_GAME_HOLDING_WEAPON);

    public static void register()
    {
        BindingRegistry.getInstance().register(SHOOT);
        BindingRegistry.getInstance().register(AIM);
        BindingRegistry.getInstance().register(RELOAD);
        BindingRegistry.getInstance().register(OPEN_ATTACHMENTS);
        BindingRegistry.getInstance().register(STEADY_AIM);
    }
}
