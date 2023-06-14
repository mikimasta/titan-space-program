package com.titan.controls;

import com.titan.gui.Titan;
import com.titan.math.Vector;
import com.titan.math.function.GravitationFunction;
import com.titan.model.CelestialObject;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

import java.util.Arrays;

public class FlightControlsTwoEngineFiresForLaunch extends Controls {

    public static final int INDEX_EARTH = 3;
    int engineFireCount = 0;

    double minDistanceToTitan = Double.MAX_VALUE;
    int stepWhenEnteredTitanOrbit = Integer.MAX_VALUE;
    int stepsNeededForOneOrbit = Integer.MAX_VALUE;
    boolean printed = false;
    boolean slow = false;

    private final Vector startingFire_1 = new Vector(new double[]{19.348420398309827, -7.455923667177558, -0.8260710537433624});
    private final Vector startingFire_2 = new Vector(new double[]{19.377969997003675, -7.467248549684882, -0.5318658649921417});

    private Vector returningFire_1_AND_2 = new Vector(new double[]{-26.522340076044202, -1.0069822166115046, 0.8590452969074249});
    // closest to Earth we can get = -26.52226378209889 -1.0069822166115046 0.8590466380119324
    public FlightControlsTwoEngineFiresForLaunch() {}

    public FlightControlsTwoEngineFiresForLaunch(Vector v) {
        super();
        this.returningFire_1_AND_2 = v;
    }

    @Override
    public void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize) {

        if((distanceToTitan(rocket, system.getTitan().getPosition()) < 1000000
                        || distanceToEarth(rocket, system) < 1000000)) {
            Titan.stepsAtOnce = 1;
        } else {
            Titan.stepsAtOnce = 50;
        }

        if (currentStep == 0) {
            Vector startVelocity = startingFire_1;
            missionLogger.log("start velocity: " + startVelocity.getLength() + " km/s");
            missionLogger.log("initial fire");
            rocket.fireEngineWithVelocity(startVelocity, stepSize, engineLogger);
            engineFireCount++;
            return;
        }

        if (currentStep == 1) {
            Vector velocity = startingFire_2;
            missionLogger.log("second velocity: " + velocity.getLength() + " km/s");
            missionLogger.log("second fire");
            rocket.fireEngineWithVelocity(velocity, stepSize, engineLogger);
            engineFireCount++;
            return;
        }

        double distanceToTitan = distanceToTitan(rocket, system.getTitan().getPosition());

        if (distanceToTitan < minDistanceToTitan) minDistanceToTitan = distanceToTitan;
        else if (!printed) {
            missionLogger.log("min distance to Titan: " + (int) minDistanceToTitan + " km");
            printed = true;
        }

        if (engineFireCount < 3 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 3000)) {
            missionLogger.log("distance to titan (center) " + (int) distanceToTitan + " km");
            missionLogger.log("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");
            missionLogger.log("third fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity()).multiplyByScalar(0.5),
                    stepSize, engineLogger);

            CelestialObject.stepsUntilNextHistoricSave = 60;
            engineFireCount++;
            return;
        } else if (engineFireCount == 3 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 300)) {
            double orbitalSpeed = getOrbitalSpeed(system.getTitan().getM(), distanceToTitan);

            missionLogger.log("distance to titan (center) " + (int) distanceToTitan + " km");
            missionLogger.log("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");
            missionLogger.log("orbital speed: " + orbitalSpeed + " km/s");
            missionLogger.log("fourth fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity())
                            .add(new Vector(new double[]{0, 0, orbitalSpeed})),
                    stepSize, engineLogger);

            stepWhenEnteredTitanOrbit = currentStep;
            double orbitLength = distanceToTitan * 2 * Math.PI;
            stepsNeededForOneOrbit = (int) ((orbitLength / orbitalSpeed) / stepSize);
            missionLogger.log("Steps for one orbit around titan: " + stepsNeededForOneOrbit);
            engineFireCount++;

            if (false) printTheWholeSystem(system);

            return;
        }

        if(currentStep == stepWhenEnteredTitanOrbit + stepsNeededForOneOrbit * 2) {
            CelestialObject.stepsUntilNextHistoricSave = 86400;
            missionLogger.log("Leaving Titan at position: " + rocket.getPosition() + "; distance to titan: " + distanceToTitan);
            Vector velocity = returningFire_1_AND_2;
            missionLogger.log("returning velocity: " + velocity.getLength() + " km/s");
            missionLogger.log("first returning fire (fifth fire total)");
            rocket.fireEngineWithVelocity(velocity, stepSize, engineLogger);
            engineFireCount++;
            return;
        }

        if(currentStep == stepWhenEnteredTitanOrbit + stepsNeededForOneOrbit * 2 + 1) {
            Vector velocity = returningFire_1_AND_2;
            missionLogger.log("2nd returning velocity: " + velocity.getLength() + " km/s");
            missionLogger.log("second returning fire (sixth fire total)");
            rocket.fireEngineWithVelocity(velocity, stepSize, engineLogger);
            engineFireCount++;
            missionLogger.log("engine will fire again when entering earth's orbit");
            return;
        }

        if (engineFireCount == 6 && distanceToEarth(rocket, system) < ((system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + 10000)) {
            missionLogger.log("distance to earth (center) " + (int) distanceToEarth(rocket, system) + " km");
            missionLogger.log("distance to earth (surface) " + (int) (distanceToEarth(rocket, system) - system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + " km");
            missionLogger.log("seventh fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithEarth(rocket, system).multiplyByScalar(0.5),
                    stepSize, engineLogger);

            CelestialObject.stepsUntilNextHistoricSave = 3600;
            engineFireCount++;
            return;
        } else if (engineFireCount == 7 && distanceToEarth(rocket, system) < ((system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + 6000)) {
            missionLogger.log("distance to earth (center) " + (int) distanceToEarth(rocket, system) + " km");
            missionLogger.log("distance to earth (surface) " + (int) (distanceToEarth(rocket, system) - system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + " km");
            missionLogger.log("eighth fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithEarth(rocket, system).multiplyByScalar(0.65),
                    stepSize, engineLogger);

            engineFireCount++;
            return;
        } else if (engineFireCount == 8 && distanceToEarth(rocket, system) < ((system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + 600)) {
            double orbitalSpeed = getOrbitalSpeed(system.getCelestialObjects().get(INDEX_EARTH).getM(), distanceToEarth(rocket, system));

            missionLogger.log("distance to earth (center) " + (int) distanceToEarth(rocket, system) + " km");
            missionLogger.log("distance to earth (center) (Vector) " +  rocket.getPosition().subtract(system.getCelestialObjects().get(INDEX_EARTH).getPosition()));
            missionLogger.log("distance to earth (surface) " + (int) (distanceToEarth(rocket, system) - system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + " km");
            missionLogger.log("orbital speed: " + orbitalSpeed + " km/s");
            missionLogger.log("final (ninth) fire");

            slow = true;

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithEarth(rocket, system)
                            .add(new Vector(new double[]{orbitalSpeed, 0, 0})),
                    stepSize, engineLogger);

            engineFireCount++;
            double orbitLength = distanceToEarth(rocket, system) * 2 * Math.PI;
            stepsNeededForOneOrbit = (int) ((orbitLength / orbitalSpeed) / stepSize);
            missionLogger.log("Steps for one orbit around earth: " + stepsNeededForOneOrbit);
            engineLogger.log("Fuel consumption history: " + Arrays.toString(rocket.getFuelConsumption().toArray()));
            engineLogger.log("Total fuel consumption: " + rocket.getFuelConsumption().stream().mapToDouble(it -> it).sum());
            return;
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

    private double distanceToEarth(Rocket rocket, SolarSystem system) {
        return system.getCelestialObjects().get(INDEX_EARTH).getPosition().subtract(rocket.getPosition()).getLength();
    }

    private Vector velocityDifferenceWithEarth(Rocket rocket, SolarSystem system) {
        return system.getCelestialObjects().get(INDEX_EARTH).getVelocity().subtract(rocket.getVelocity());
    }

    private void printTheWholeSystem(SolarSystem system) {
        for (CelestialObject o : system.getCelestialObjects()) {
            System.out.print(o.getName() + ",");
            System.out.print(o.getPosition().getValue(0) + ",");
            System.out.print(o.getPosition().getValue(1) + ",");
            System.out.print(o.getPosition().getValue(2) + ",");
            System.out.print(o.getVelocity().getValue(0) + ",");
            System.out.print(o.getVelocity().getValue(1) + ",");
            System.out.print(o.getVelocity().getValue(2) + ",");
            System.out.print(o.getM() + ",");
            System.out.print(o.getDiameter() + ",");
            System.out.print(o.getColor().toString() + ",");
            System.out.print(o.getRadius() + ",");
        }

    }
}
