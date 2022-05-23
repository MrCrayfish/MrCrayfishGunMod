package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class SineEaseInOut extends BaseEasing {
    @Override
    protected Float calculate(float x, float b, float c) {
        return (-((float)Math.cos(3.14 * x) - 1) / 2) * c + b;
    }
}
