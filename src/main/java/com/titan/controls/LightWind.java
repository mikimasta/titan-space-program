package com.titan.controls;

import com.titan.math.Vector;
import com.titan.model.LandingModule;

public class LightWind extends Wind { // 2 km/h

    private Vector currentWindSpeed = new Vector(new double[]{0,0,0});

    @Override
    Vector calculateVelocity(LandingModule module) {
        if (module.getY() > 0.06) {

            currentWindSpeed = new Vector(new double[]{-50d/3600d, 0, 0});
            return currentWindSpeed;
        }
        return new Vector(new double[]{0, 0, 0});
    }

    @Override
    public double getWindSpeed() {
        return currentWindSpeed.getLength() * 3600;
    }

    @Override
    public double getWindAngle() {

        double angle  = Math.asin(currentWindSpeed.getValue(1)/currentWindSpeed.getLength());


        return currentWindSpeed.getValue(0) < 0 ? 180 - Math.toDegrees(angle) : Math.toDegrees(angle);//currentWindSpeed.getValue(0) < 0 ? -degrees : degrees;

    }
}
