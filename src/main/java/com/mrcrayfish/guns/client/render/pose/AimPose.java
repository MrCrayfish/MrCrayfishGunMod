package com.mrcrayfish.guns.client.render.pose;

import com.mojang.math.Vector3f;

/**
 * Author: MrCrayfish
 */
public class AimPose
{
    private Instance idle = new Instance();
    private Instance aiming = new Instance();

    public Instance getIdle()
    {
        return this.idle;
    }

    public Instance getAiming()
    {
        return this.aiming;
    }

    public static class Instance
    {
        private LimbPose leftArm = new LimbPose();
        private LimbPose rightArm = new LimbPose();
        private float renderYawOffset = 0F;
        private Vector3f itemTranslate = new Vector3f();
        private Vector3f itemRotation = new Vector3f();

        public LimbPose getLeftArm()
        {
            return this.leftArm;
        }

        public Instance setLeftArm(LimbPose leftArm)
        {
            this.leftArm = leftArm;
            return this;
        }

        public LimbPose getRightArm()
        {
            return this.rightArm;
        }

        public Instance setRightArm(LimbPose rightArm)
        {
            this.rightArm = rightArm;
            return this;
        }

        public float getRenderYawOffset()
        {
            return this.renderYawOffset;
        }

        public Instance setRenderYawOffset(float renderYawOffset)
        {
            this.renderYawOffset = renderYawOffset;
            return this;
        }

        public Vector3f getItemTranslate()
        {
            return this.itemTranslate;
        }

        public Instance setItemTranslate(Vector3f itemTranslate)
        {
            this.itemTranslate = itemTranslate;
            return this;
        }

        public Vector3f getItemRotation()
        {
            return this.itemRotation;
        }

        public Instance setItemRotation(Vector3f itemRotation)
        {
            this.itemRotation = itemRotation;
            return this;
        }
    }
}
