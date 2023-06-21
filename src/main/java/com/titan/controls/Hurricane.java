package com.titan.controls;

import com.titan.math.Vector;

public class Hurricane extends Wind {

    @Override
    Vector calculateVelocity() {
        return new Vector(new double[]{120d/3600d, 0, 0});
    }
}