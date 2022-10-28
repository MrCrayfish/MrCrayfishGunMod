package com.tac.guns.util.math;

import java.util.Date;
import java.util.Random;

public class OneDimensionalPerlinNoise {
    private final Random random = new Random();

    private final float rangeDown;

    private final float rangeUp;

    private final long periodMs;

    private float prevNum;

    private float num;

    private long prevTime = new Date().getTime();

    private boolean reverse = false;

    public OneDimensionalPerlinNoise(float rangeDown, float rangeUp, long periodMs){
        this.rangeDown = rangeDown;
        this.rangeUp = rangeUp;
        this.periodMs = periodMs;
        prevNum = random.nextFloat() * (rangeUp - rangeDown) + rangeDown;
        num = random.nextFloat() * (rangeUp - rangeDown) + rangeDown;
        if(reverse && prevNum * num > 0) num = -num;
    }

    public void setReverse(boolean reverse) {this.reverse = reverse;}

    public static double Ease_Interpolate(double x)
    {
        return (3 * Math.pow(x, 2) - 2 * Math.pow(x, 3));
    }

    public float getValue(){
        long periodTime = new Date().getTime() - prevTime;
        long repeat = periodTime / periodMs;
        long partialTime = periodTime % periodMs;
        prevTime += repeat * periodMs;
        double x = Ease_Interpolate((double) partialTime / (double) periodMs);
        if(repeat == 1){
            prevNum = num;
            num =  random.nextFloat() * (rangeUp - rangeDown) + rangeDown;
            if(reverse && prevNum * num > 0) num = -num;
        }else if(repeat > 1){
            prevNum = random.nextFloat() * (rangeUp - rangeDown) + rangeDown;
            num =  random.nextFloat() * (rangeUp - rangeDown) + rangeDown;
            if(reverse && prevNum * num > 0) num = -num;
        }
        return (float) (prevNum*(1 - x) + num * x);
    }
}
