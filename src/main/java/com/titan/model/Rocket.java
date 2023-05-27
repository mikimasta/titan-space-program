package com.titan.model;

import com.titan.gui.Titan;
import com.titan.math.Vector;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Rocket extends CelestialObject {

    private final ArrayList<Double> fuelConsumption = new ArrayList<>();

    public Rocket(String name, double m, Vector initialPos, Vector initialVel, double diameter, Color color, int radius) {
        super(name, m, initialPos, initialVel, diameter, color, radius);
    }

    public void fireEngineWithForce(Vector force, int stepSize) {
        if (Titan.log) System.out.println("force: " + force + "; length: " + force.getLength() + " N == kg * m/s^2");
        Vector impulse = force.multiplyByScalar(stepSize); // kg * m/s
        impulse = impulse.multiplyByScalar(1.0/1000); // kg * km/s
        double fuel = impulse.getLength() * getM() * (1.0/stepSize);
        if (Titan.log) System.out.println("fuel consumed: " + fuel);
        fuelConsumption.add(fuel);
        Vector velocity = impulse.multiplyByScalar(1.0/getM()); // km/s
        updateVelocity(getVelocity().add(velocity));
    }

    public void fireEngineWithVelocity(Vector velocity, int stepSize) {
        velocity = velocity.multiplyByScalar(1000); // km/s => m/s
        Vector force = velocity.multiplyByScalar(getM()).multiplyByScalar(1.0/stepSize); // kg * m/s^2 == N
        fireEngineWithForce(force, stepSize);
    }

    public ArrayList<Double> getFuelConsumption() {
        return fuelConsumption;
    }
}
