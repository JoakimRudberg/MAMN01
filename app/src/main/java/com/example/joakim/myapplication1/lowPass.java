package com.example.joakim.myapplication1;

/**
 * Created by Joakim on 2018-03-25.
 */

public class lowPass {
    private static final float ALPHA = 0.50f;

    protected static float[] lowPassFilter(float[] currentValue, float[] preValue) {
        if (preValue == null) {
            return currentValue;
        }
        for ( int i=0; i<currentValue.length; i++ ) {
                if ((currentValue[i]-preValue[i]) > 0.5 || (currentValue[i]-preValue[i]) < -0.5){
                    preValue[i] = preValue[i] + ALPHA * (currentValue[i] - preValue[i]);
                }
        }
        return preValue;
    }
}
