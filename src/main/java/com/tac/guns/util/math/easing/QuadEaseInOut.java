package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class QuadEaseInOut extends BaseEasing {
    @Override
    protected Float calculate(float t, float b, float c) {
        return ( t < 0.5 ? 2 * t * t : 1 - (float)Math.pow(-2 * t + 2, 2) / 2)*c +b;
    }
}
