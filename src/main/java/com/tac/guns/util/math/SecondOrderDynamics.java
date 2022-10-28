package com.tac.guns.util.math;

import net.minecraft.util.math.vector.Vector3f;

public class SecondOrderDynamics {
    private final float k1;
    private final float k2;
    private final float k3 ;

    private float py;
    private float pyd;
    private float px;

    /**
     * @param f Natural frequency
     * @param z Damping coefficient
     * @param r Initial velocity
     * @param x0 Initial position
     * */
    public SecondOrderDynamics(float f, float z, float r, float x0){
        k1 = (float) (z / (Math.PI * f));
        k2 = (float) (1 / ((2 * Math.PI * f) * (2 * Math.PI * f)));
        k3 = (float) (r * z / (2 * Math.PI * f));

        py = px = x0;
        pyd = 0;
    }

    /**
     * @return processed y value
     * */
    public float update(float T, float x){
        float xd = (x - px) / T;
        float y = py + T * pyd;

        pyd = pyd + T * (px + k3 * xd - py - k1 * pyd) / k2;
        px = x;
        py = y;
        return y;
    }
}
