package com.titan.controls;

import com.titan.math.Vector;
import com.titan.math.function.GravitationFunction;
import com.titan.model.CelestialObject;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

public class FirstTestControls extends Controls {

    int engineFireCount = 0;

    double minDistanceToTitan = 1e20;
    boolean printed = false;

    @Override
    public void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize) {

        // System.out.println(system.getTitan().getPosition().subtract(system.getCelestialObjects().get(7).getPosition()).getLength());

        if (currentStep == 0) {
            Vector startVelocity = new Vector(new double[]{38.696840796619654, -14.911847334355116, -1.6521421074867249});
            System.out.println("start velocity: " + startVelocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(startVelocity, stepSize);
            engineFireCount++;
            System.out.println("initial fire");
            return;
        }

        double distanceToTitan = distanceToTitan(rocket, system.getTitan().getPosition());

        if (distanceToTitan < minDistanceToTitan) minDistanceToTitan = distanceToTitan;
        else if (!printed) {
            System.out.println("min distance to Titan: " + (int) minDistanceToTitan + " km");
            printed = true;
        }

        if (engineFireCount < 2 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 200)) {
            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity()),
                    stepSize);

            double orbitalSpeed = getOrbitalSpeed(system.getTitan().getM(), distanceToTitan);
            System.out.println("distance to titan (center) " + (int) distanceToTitan + " km");
            System.out.println("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");
            System.out.println("orbital speed: " + orbitalSpeed + " km/s");

            rocket.fireEngineWithVelocity(
                    new Vector(new double[]{0, 0, orbitalSpeed}),
                    stepSize);
            CelestialObject.stepsUntilNextHistoricSave = 3600;
            engineFireCount++;
            System.out.println("second fire");
        }
    }

    /**
     * calculates the velocity an object has to have to be on an orbit around a planet/moon etc. <p>
     * velocity = sqrt((gravitational_constant * mass_of_the_planet / distance))
     * @param mass mass of the planet/moon
     * @param distance distance to the planet's/moon's center of mass
     * @return velocity
     */
    private double getOrbitalSpeed(double mass, double distance) {
        return Math.sqrt((GravitationFunction.G * mass / distance));
    }

    private double distanceToTitan(Rocket rocket, Vector titanPosition) {
        return rocket.getPosition().subtract(titanPosition).getLength();
    }

    private Vector velocityDifferenceWithTitan(Rocket rocket, Vector titanVelocity) {
        return titanVelocity.subtract(rocket.getVelocity());
    }
}
