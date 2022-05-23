package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class QuadEaseOut extends BaseEasing {
    @Override
    protected Float calculate(float t, float b, float c) {
        return -c *t*(t-2) + b;
    }
}
