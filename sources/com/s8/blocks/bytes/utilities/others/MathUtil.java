package com.s8.blocks.bytes.utilities.others;

/**
 * Created by Adel Abbas on 09/04/2017.
 */
public class MathUtil {
    public static float constrain(float value, float min, float max){
        if(value > max){ return  max;}
        if(value < min){ return min;}
        return value;
    }
}
