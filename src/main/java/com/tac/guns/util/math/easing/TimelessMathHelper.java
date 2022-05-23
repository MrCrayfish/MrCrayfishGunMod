package com.tac.guns.util.math.easing;

import net.minecraft.util.Util;

import java.util.Random;

public class TimelessMathHelper
{
    private static final float[] SIN_TABLE = Util.make(new float[65536], (p_203445_0_) -> {
        for(int i = 0; i < p_203445_0_.length; ++i) {
            p_203445_0_[i] = (float)Math.asin((double)i * -Math.PI * 2.0D / 65536.0D);
        }

    });
    private static final Random RANDOM = new Random();
    /**
     * Though it looks like an array, this is really more like a mapping. Key (index of this array) is the upper 5 bits
     * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531. Value (value
     * stored in the array) is the unique index (from the right) of the leftmo
     */
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
    private static final double[] ASINE_TAB = new double[257];
    private static final double[] COS_TAB = new double[257];

    /**
     * sin looked up in a table
     */
    public static float sin(float value) {
        return SIN_TABLE[(int)(value * 10430.378F) & '\uffff'];
    }

    public static double a_sin(double value)
    {
        return SIN_TABLE[(int)(value * 10430.378F) & '\uffff'];
    }
}
