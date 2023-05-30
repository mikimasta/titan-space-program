package com.titan.controls;

import com.titan.math.Vector;
import com.titan.math.function.GravitationFunction;
import com.titan.model.CelestialObject;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

public class SecondTestControls extends Controls {

    int engineFireCount = 0;
    
    double minDistanceToTitan = 1e20;
    boolean printed = false;

    Vector secondVelocity;

    public SecondTestControls(Vector secondVelocity) {
        this.secondVelocity = secondVelocity;
    }

    @Override
    public void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize) {

        // if (missionLogger.isLogging()) missionLogger.log(system.getTitan().getPosition().subtract(system.getCelestialObjects().get(7).getPosition()).getLength());

        if (currentStep == 0) {
            Vector startVelocity = new Vector(new double[]{19.348420398309827, -7.455923667177558, -0.8260710537433624});
            if (missionLogger.isLogging()) missionLogger.log("start velocity: " + startVelocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(startVelocity, stepSize);
            engineFireCount++;
            if (missionLogger.isLogging()) missionLogger.log("initial fire");
            return;
        }

        if (currentStep == 1) {
            Vector velocity = secondVelocity;
            if (missionLogger.isLogging()) missionLogger.log("second velocity: " + velocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(velocity, stepSize);
            engineFireCount++;
            if (missionLogger.isLogging()) missionLogger.log("second fire");
            return;
        }

        double distanceToTitan = distanceToTitan(rocket, system.getTitan().getPosition());

        if (distanceToTitan < minDistanceToTitan) minDistanceToTitan = distanceToTitan;
        else if (!printed) {
            if (missionLogger.isLogging()) missionLogger.log("min distance to Titan: " + (int) minDistanceToTitan + " km");
            printed = true;
        }

        if (engineFireCount < 2 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 200)) {
            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity()),
                    stepSize);

            double orbitalSpeed = getOrbitalSpeed(system.getTitan().getM(), distanceToTitan);
            if (missionLogger.isLogging()) missionLogger.log("distance to titan (center) " + (int) distanceToTitan + " km");
            if (missionLogger.isLogging()) missionLogger.log("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");
            if (missionLogger.isLogging()) missionLogger.log("orbital speed: " + orbitalSpeed + " km/s");

            rocket.fireEngineWithVelocity(
                    new Vector(new double[]{0, 0, orbitalSpeed}),
                    stepSize);
            CelestialObject.stepsUntilNextHistoricSave = 3600;
            engineFireCount++;
            if (missionLogger.isLogging()) missionLogger.log("third fire");
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
