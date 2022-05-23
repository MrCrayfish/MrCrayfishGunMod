package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class BackEaseOut extends BaseEasing {
    private final float c1 = 1.70158f;
    private final float c3 = c1 + 1;
    @Override
    protected Float calculate(float x, float b, float c) {
        return (1 + c3 * (float) Math.pow(x - 1, 3) + c1 * (float) Math.pow(x - 1, 2))*c + b;
    }
}