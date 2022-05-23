package com.tac.guns.util.math.easing;

import com.tac.guns.util.math.easing.BaseEasing;

public class CircEaseIn extends BaseEasing {
    @Override
    protected Float calculate(float x, float b, float c) {
        return ( 1 - (float)Math.sqrt(1 - Math.pow(x, 2))) * c + b;
    }
}