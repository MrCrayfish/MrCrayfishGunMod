package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class QuadEaseIn extends BaseEasing {
    @Override
    protected Float calculate(float t, float b, float c) {
        return t*t*c + b;
    }
}
