package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class QuartEaseInOut extends BaseEasing {
    @Override
    protected Float calculate(float x, float b, float c) {
        return (x < 0.5 ? 8 * x * x * x * x : 1 - (float)Math.pow(-2 * x + 2, 4) / 2)*c + b;
    }
}
