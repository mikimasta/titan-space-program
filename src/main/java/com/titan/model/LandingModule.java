package com.titan.model;

import com.titan.math.Vector;
import javafx.scene.paint.Color;

public class LandingModule extends Rocket {


    private Vector thrust = new Vector(new double[]{0, 0});

    public LandingModule(String name, double m, Vector initialPos, Vector initialVel, double diameter, Color color, int radius) {
        super(name, m, initialPos, initialVel, diameter, color, radius);
    }

    public LandingModule(String name, double m, double diameter, Color color, int radius) {
        super(name, m, new Vector(new double[]{0, 182, 0}), new Vector(new double[]{ 1.8076753043527565, 0, 0}), diameter, color, radius);
    }

    public LandingModule(String name) {
        //super(name, 50000, new Vector(new double[]{-180, 182, 0}), new Vector(new double[]{ 1.8076753043527565, 0, 0}), 10, Color.SILVER, 2);
        super(name, 50000, new Vector(new double[]{-180, 182, 0}), new Vector(new double[]{ 1.8076753043527565, 0, 0}), 10, Color.SILVER, 2);
        //super(name, 50000, new Vector(new double[]{0, 182, 0}), new Vector(new double[]{ 0, 0, 0}), 10, Color.SILVER, 2);
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

    public double getVelocityInKmPerSecond() {
        return getVelocity().getLength();
    }

    public Vector getThrust() {
        return thrust;
    }

    public void applyEngineForces(double accelerationForce, double rotation) {
        thrust = new Vector(new double[] {
                accelerationForce / this.getM(), // force / mass = N/kg = kg * m / s^2 / kg = m / s^2
                rotation});
    }

    public double getTotalSpeed() {
        return new Vector(new double[]{getVelocity().getValue(0), getVelocity().getValue(1)}).getLength();
    }

    public void setThrust(Vector thrust) {
        this.thrust = thrust;
    }
}
