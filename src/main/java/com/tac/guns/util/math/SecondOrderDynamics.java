package com.tac.guns.util.math;

import net.minecraft.util.math.vector.Vector3f;

public class SecondOrderDynamics {
    private final float k1;
    private final float k2;
    private final float k3 ;
    private final Vector3f y; //position
    private final Vector3f yd; //velocity
    private Vector3f xp; //previous input

    /**
     * @param f Natural frequency
     * @param z Damping coefficient
     * @param r Initial response
     * @param x0 Initial position
     * */
    public SecondOrderDynamics(float f, float z, float r,Vector3f x0){
        k1 = (float) (z / (Math.PI * f));
        k2 = (float) (1 / ((2 * Math.PI * f) * (2 * Math.PI * f)));
        k3 = (float) (r * z / (2 * Math.PI * f));

        xp = x0;
        y = x0;
        yd = new Vector3f(0,0,0);
    }

    /**
     * @return processed position vector
     * */
    public Vector3f update(float T, Vector3f x){
        Vector3f xd = x.copy();
        xd.sub(xp);
        xd.mul(1 / T);
        xp = x;

        Vector3f deltaY = yd.copy();  deltaY.mul(T);
        y.add(deltaY);

        Vector3f deltaYd = x.copy();
        Vector3f deltaXd = xd.copy();  deltaXd.mul(k3);  //k3 * xd
        Vector3f alphaYd = yd.copy();  alphaYd.mul(k1);  //k1 * yd
        deltaYd.add(deltaXd);
        deltaYd.sub(alphaYd);
        deltaYd.sub(y);
        deltaYd.mul(T / k2);
        yd.add(deltaYd);

        return y;
    }
}
