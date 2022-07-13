package com.mrcrayfish.guns.client;

import com.mojang.math.Vector3f;

/**
 * Author: MrCrayfish
 */
public enum SwayType
{
    DIRECTIONAL(Vector3f.XN, Vector3f.YN),
    DRAG(Vector3f.XP, Vector3f.YP);

    Vector3f pitchRotation;
    Vector3f yawRotation;

    SwayType(Vector3f pitchRotation, Vector3f yawRotation)
    {
        this.pitchRotation = pitchRotation;
        this.yawRotation = yawRotation;
    }

    public Vector3f getPitchRotation()
    {
        return this.pitchRotation;
    }

    public Vector3f getYawRotation()
    {
        return this.yawRotation;
    }
}
