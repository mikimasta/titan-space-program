package com.titan.controls;

import com.titan.gui.Titan;
import com.titan.math.Vector;
import com.titan.math.function.GravitationFunction;
import com.titan.model.CelestialObject;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

public class FlightControlsTwoEngineFiresForLaunch implements Controls {

    int engineFireCount = 0;

    double minDistanceToTitan = Double.MAX_VALUE;
//    int stepWhenEnteredTitanOrbit = 0;
    int stepWhenEnteredTitanOrbit = Integer.MAX_VALUE;
//    int stepsNeededForOneOrbit = 159;
    int stepsNeededForOneOrbit = Integer.MAX_VALUE;
    boolean printed = false;
    boolean slow = false;
    boolean log = false;

    private Vector startingFire_1 = new Vector(new double[]{19.348420398309827, -7.455923667177558, -0.8260710537433624});
    private Vector startingFire_2 = new Vector(new double[]{19.377969997003675, -7.467248549684882, -0.5318658649921417});

//    private Vector returningFire_1_AND_2 = new Vector(new double[]{-19.348420398309827, 7.455923667177558, 0.8260710537433624});
    private Vector returningFire_1_AND_2 = new Vector(new double[]{-19.348420398309827, 7.455923667177558, 2.9588835537433624});

    public FlightControlsTwoEngineFiresForLaunch(Vector returningFire_1_AND_2) {
        this.returningFire_1_AND_2 = returningFire_1_AND_2;
    }

    public FlightControlsTwoEngineFiresForLaunch() {}

    @Override
    public void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize) {

        // if(log) System.out.println(system.getTitan().getPosition().subtract(system.getCelestialObjects().get(7).getPosition()).getLength());

        if(slow && (distanceToTitan(rocket, system.getTitan().getPosition()) < 1000000
                        || distanceToEarth(rocket, system) < 1000000)) {
            Titan.stepsAtOnce = 1;
            slow = false;
        } else if(!slow) {
            Titan.stepsAtOnce = 5;
            slow = true;
        }

        if (currentStep == 0) {
            Vector startVelocity = startingFire_1;
            if(log) System.out.println("start velocity: " + startVelocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(startVelocity, stepSize);
            engineFireCount++;
            if(log) System.out.println("initial fire");
            return;
        }

        if (currentStep == 1) {
            Vector velocity = startingFire_2;
            if(log) System.out.println("second velocity: " + velocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(velocity, stepSize);
            engineFireCount++;
            if(log) System.out.println("second fire");
            return;
        }

        double distanceToTitan = distanceToTitan(rocket, system.getTitan().getPosition());

        if (distanceToTitan < minDistanceToTitan) minDistanceToTitan = distanceToTitan;
        else if (!printed) {
            if(log) System.out.println("min distance to Titan: " + (int) minDistanceToTitan + " km");
            printed = true;
        }

        if (engineFireCount < 3 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 3000)) {
            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity()).multiplyByScalar(0.5),
                    stepSize);

            if(log) System.out.println("distance to titan (center) " + (int) distanceToTitan + " km");
            if(log) System.out.println("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");

            CelestialObject.stepsUntilNextHistoricSave = 3600;
            engineFireCount++;
            if(log) System.out.println("third fire");
            return;
        } else if (engineFireCount == 3 && distanceToTitan < (system.getTitan().getDiameter() / 2 + 300)) {
            double orbitalSpeed = getOrbitalSpeed(system.getTitan().getM(), distanceToTitan);
            rocket.fireEngineWithVelocity(
                    velocityDifferenceWithTitan(rocket, system.getTitan().getVelocity())
                            .add(new Vector(new double[]{0, 0, orbitalSpeed})),
                    stepSize);

            if(log) System.out.println("distance to titan (center) " + (int) distanceToTitan + " km");
            if(log) System.out.println("distance to titan (surface) " + (int) (distanceToTitan - system.getTitan().getDiameter() / 2) + " km");
            if(log) System.out.println("orbital speed: " + orbitalSpeed + " km/s");

            stepWhenEnteredTitanOrbit = currentStep;
            double orbitLength = distanceToTitan * 2 * Math.PI;
            stepsNeededForOneOrbit = (int) ((orbitLength / orbitalSpeed) / stepSize);
            if(log) System.out.println("Steps for one orbit around titan: " + stepsNeededForOneOrbit);
            engineFireCount++;
            if(log) System.out.println("fourth fire");

            /*
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
             */

            return;
        }

        if(currentStep == stepWhenEnteredTitanOrbit + stepsNeededForOneOrbit * 2) {
            Vector velocity = returningFire_1_AND_2;
            if(log) System.out.println("returning velocity: " + velocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(velocity, stepSize);
            engineFireCount++;
            if(log) System.out.println("first returning fire");
            return;
        }

        if(currentStep == stepWhenEnteredTitanOrbit + stepsNeededForOneOrbit * 2 + 1) {
            Vector velocity = returningFire_1_AND_2;
            if(log) System.out.println("2nd returning velocity: " + velocity.getLength() + " km/s");
            rocket.fireEngineWithVelocity(velocity, stepSize);
            engineFireCount++;
            if(log) System.out.println("second returning fire");
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
        return system.getCelestialObjects().get(3).getPosition().subtract(rocket.getPosition()).getLength();
    }

    private Vector velocityDifferenceWithEarth(Rocket rocket, SolarSystem system) {
        return system.getCelestialObjects().get(3).getVelocity().subtract(rocket.getVelocity());
    }
}
