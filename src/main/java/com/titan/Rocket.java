package com.titan;

import com.titan.math.Vector;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Rocket extends CelestialObject {

    private ArrayList<Double> fuelConsumption = new ArrayList<>();

    /**
     * constructs a Rocket
     * @param name
     * @param m
     * @param initialPos
     * @param initialVel
     * @param diameter
     * @param color
     * @param radius
     */
    public Rocket(String name, double m, Vector initialPos, Vector initialVel, double diameter, Color color, int radius) {
        super(name, m, initialPos, initialVel, diameter, color, radius);
    }

    public void fireEngine(Vector force, int stepSize) {
        Vector impulse = force.multiplyByScalar(stepSize);
        double fuel = impulse.getLength() * getM() * (1.0/stepSize);
        fuelConsumption.add(fuel);
        updateVelocity(getVelocity().add(impulse.multiplyByScalar(1/getM())));
    }

    public ArrayList<Double> getFuelConsumption() {
        return fuelConsumption;
    }

}
