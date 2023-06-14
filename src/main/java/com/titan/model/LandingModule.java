package com.titan.model;

import com.titan.math.Vector;
import javafx.scene.paint.Color;

public class LandingModule extends Rocket {

    public LandingModule(String name, double m, Vector initialPos, Vector initialVel, double diameter, Color color, int radius) {
        super(name, m, initialPos, initialVel, diameter, color, radius);
    }

    public LandingModule(String name, double m, double diameter, Color color, int radius) {
        super(name, m, new Vector(new double[]{0, 182, 0}), new Vector(new double[]{ 1.8076753043527565, 0, 0}), diameter, color, radius);
    }

    public LandingModule(String name) {
        super(name, 50000, new Vector(new double[]{0, 182, 0}), new Vector(new double[]{ 1.8076753043527565, 0, 0}), 10, Color.SILVER, 2);
    }

    public double getX() {
        return getPosition().getValue(0);
    }

    public double getY() {
        return getPosition().getValue(1);
    }

    public double getRotationAngle() {
        return getPosition().getValue(2);
    }

}
