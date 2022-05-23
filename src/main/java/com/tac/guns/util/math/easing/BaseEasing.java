package com.tac.guns.util.math.easing;

// Author: Mayday of Timeless Development
public abstract class BaseEasing {

    public final Float evaluate(float fraction, Number startValue, Number endValue){
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        return calculate(fraction,b,c);
    }

    protected abstract Float calculate(float t, float b, float c);
}
