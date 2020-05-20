package com.mrcrayfish.guns.client;

/**
 * Author: MrCrayfish
 */
public class Hooks
{
    /**
     * Linked via ASM. Modifies the sensitivity of the mouse while aiming down the sight of guns.
     *
     * @param sensitivity the input mouse sensitivity
     * @return new mouse sensitivity
     */
    @SuppressWarnings("unused")
    public static double applyModifiedSensitivity(double sensitivity)
    {
        return ClientHandler.getGunRenderer().applyZoomSensitivity(sensitivity);
    }
}
