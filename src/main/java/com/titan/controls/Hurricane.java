package com.titan.controls;

import com.titan.math.Vector;
import com.titan.model.LandingModule;

public class Hurricane extends Wind {

    @Override
    Vector calculateVelocity(LandingModule module) {
        if (module.getX() > 0.05) {
            return new Vector(new double[]{120d/3600d, 0, 0});
        }
        return new Vector(new double[]{0, 0, 0});
    }
}