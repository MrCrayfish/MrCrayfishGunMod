package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class CircEaseInOut extends BaseEasing {
    @Override
    protected Float calculate(float x, float b, float c) {
        return ( x < 0.5
                ? (1 - (float)Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
                : ((float)Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2) * c + b;
    }
}
