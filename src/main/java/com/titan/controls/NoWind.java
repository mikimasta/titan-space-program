package com.titan.controls;

import com.titan.math.Vector;
import com.titan.model.LandingModule;

public class NoWind extends Wind {

    @Override
    Vector calculateVelocity(LandingModule module) {
        return new Vector(new double[]{0, 0, 0});
    }

    @Override
    public double getWindSpeed() {
        return 0;
    }

    @Override
    public double getWindAngle() {
        return 0d;
    }

}
