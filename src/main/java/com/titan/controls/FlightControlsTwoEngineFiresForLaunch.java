package com.titan.controls;

import com.titan.gui.Titan;
import com.titan.math.Vector;
import com.titan.math.function.GravitationFunction;
import com.titan.model.CelestialObject;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

import java.util.Arrays;

public class FlightControlsTwoEngineFiresForLaunch implements Controls {

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
            if(Titan.log) System.out.println("start velocity: " + startVelocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(startVelocity, stepSize);
            engineFireCount++;
            if(Titan.log) System.out.println("initial fire");
            return;
        }

        if (currentStep == 1) {
            Vector velocity = startingFire_2;
            if(Titan.log) System.out.println("second velocity: " + velocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(velocity, stepSize);
            engineFireCount++;
            if(Titan.log) System.out.println("second fire");
            return;
        }

        double distanceToTitan = distanceToTitan(rocket, system.getTitan().getPosition());

        if (distanceToTitan < minDistanceToTitan) minDistanceToTitan = distanceToTitan;
        else if (!printed) {
            if(Titan.log) System.out.println("min distance to Titan: " + (int) minDistanceToTitan + " km");
            printed = true;
        }

        if (engineFireCount < 3 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 3000)) {
            if(Titan.log) System.out.println("distance to titan (center) " + (int) distanceToTitan + " km");
            if(Titan.log) System.out.println("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");
            if(Titan.log) System.out.println("third fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity()).multiplyByScalar(0.5),
                    stepSize);

            CelestialObject.stepsUntilNextHistoricSave = 60;
            engineFireCount++;
            return;
        } else if (engineFireCount == 3 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 300)) {
            double orbitalSpeed = getOrbitalSpeed(system.getTitan().getM(), distanceToTitan);

            if(Titan.log) System.out.println("distance to titan (center) " + (int) distanceToTitan + " km");
            if(Titan.log) System.out.println("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");
            if(Titan.log) System.out.println("orbital speed: " + orbitalSpeed + " km/s");
            if(Titan.log) System.out.println("fourth fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity())
                            .add(new Vector(new double[]{0, 0, orbitalSpeed})),
                    stepSize);

            stepWhenEnteredTitanOrbit = currentStep;
            double orbitLength = distanceToTitan * 2 * Math.PI;
            stepsNeededForOneOrbit = (int) ((orbitLength / orbitalSpeed) / stepSize);
            if(Titan.log) System.out.println("Steps for one orbit around titan: " + stepsNeededForOneOrbit);
            engineFireCount++;

            if (false) printTheWholeSystem(system);

            return;
        }

        if(currentStep == stepWhenEnteredTitanOrbit + stepsNeededForOneOrbit * 2) {
            CelestialObject.stepsUntilNextHistoricSave = 86400;
            if(Titan.log) System.out.println("Leaving Titan at position: " + rocket.getPosition() + "; distance to titan: " + distanceToTitan);
            Vector velocity = returningFire_1_AND_2;
            if(Titan.log) System.out.println("returning velocity: " + velocity.getLength() + " km/s");
            if(Titan.log) System.out.println("first returning fire (fifth fire total)");
            rocket.fireEngineWithVelocity(velocity, stepSize);
            engineFireCount++;
            return;
        }

        if(currentStep == stepWhenEnteredTitanOrbit + stepsNeededForOneOrbit * 2 + 1) {
            Vector velocity = returningFire_1_AND_2;
            if(Titan.log) System.out.println("2nd returning velocity: " + velocity.getLength() + " km/s");
            if(Titan.log) System.out.println("second returning fire (sixth fire total)");
            rocket.fireEngineWithVelocity(velocity, stepSize);
            engineFireCount++;
            if(Titan.log) System.out.println("engine will fire again when entering earth's orbit");
            return;
        }

        if (engineFireCount == 6 && distanceToEarth(rocket, system) < ((system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + 10000)) {
            if(Titan.log) System.out.println("distance to earth (center) " + (int) distanceToEarth(rocket, system) + " km");
            if(Titan.log) System.out.println("distance to earth (surface) " + (int) (distanceToEarth(rocket, system) - system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + " km");
            if(Titan.log) System.out.println("seventh fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithEarth(rocket, system).multiplyByScalar(0.5),
                    stepSize);

            CelestialObject.stepsUntilNextHistoricSave = 3600;
            engineFireCount++;
            return;
        } else if (engineFireCount == 7 && distanceToEarth(rocket, system) < ((system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + 6000)) {
            if(Titan.log) System.out.println("distance to earth (center) " + (int) distanceToEarth(rocket, system) + " km");
            if(Titan.log) System.out.println("distance to earth (surface) " + (int) (distanceToEarth(rocket, system) - system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + " km");
            if(Titan.log) System.out.println("eighth fire");

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithEarth(rocket, system).multiplyByScalar(0.65),
                    stepSize);

            engineFireCount++;
            return;
        } else if (engineFireCount == 8 && distanceToEarth(rocket, system) < ((system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + 600)) {
            double orbitalSpeed = getOrbitalSpeed(system.getCelestialObjects().get(INDEX_EARTH).getM(), distanceToEarth(rocket, system));

            if(Titan.log) System.out.println("distance to earth (center) " + (int) distanceToEarth(rocket, system) + " km");
            if(Titan.log) System.out.println("distance to earth (center) (Vector) " +  rocket.getPosition().subtract(system.getCelestialObjects().get(INDEX_EARTH).getPosition()));
            if(Titan.log) System.out.println("distance to earth (surface) " + (int) (distanceToEarth(rocket, system) - system.getCelestialObjects().get(INDEX_EARTH).getDiameter()/2) + " km");
            if(Titan.log) System.out.println("orbital speed: " + orbitalSpeed + " km/s");
            if(Titan.log) System.out.println("final (ninth) fire");

            slow = true;

            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithEarth(rocket, system)
                            .add(new Vector(new double[]{orbitalSpeed, 0, 0})),
                    stepSize);

            engineFireCount++;
            double orbitLength = distanceToEarth(rocket, system) * 2 * Math.PI;
            stepsNeededForOneOrbit = (int) ((orbitLength / orbitalSpeed) / stepSize);
            if(Titan.log) System.out.println("Steps for one orbit around earth: " + stepsNeededForOneOrbit);
            if(Titan.log) System.out.println("Fuel consumption history: " + Arrays.toString(rocket.getFuelConsumption().toArray()));
            if(Titan.log) System.out.println("Total fuel consumption: " + rocket.getFuelConsumption().stream().mapToDouble(it -> it).sum());
            return;
        }


    }

    /**
     * calculates the velocity an object has to have to be on an orbit around a planet/moon etc. <p>
     * velocity = √(gravitational_constant * mass_of_the_planet / distance)
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
        System.out.println();
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
            System.out.print("YELLOW,");
            System.out.print(o.getRadius() + ",");
            System.out.println();
        }

    }
}
