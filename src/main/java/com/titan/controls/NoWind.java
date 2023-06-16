package com.titan.controls;

import com.titan.math.Vector;

public class NoWind extends Wind {

    @Override
    Vector calculateVelocity() {
        return new Vector(new double[]{0, 0, 0});
    }
}