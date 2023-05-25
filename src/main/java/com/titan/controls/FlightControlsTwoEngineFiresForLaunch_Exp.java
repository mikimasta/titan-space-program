package com.titan.controls;

import com.titan.math.Vector;
import com.titan.math.function.GravitationFunction;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

public class FlightControlsTwoEngineFiresForLaunch_Exp implements Controls {

    int engineFireCount = 0;

    double minDistanceToTitan = Double.MAX_VALUE;
    int stepWhenEnteredTitanOrbit = 0;
//    int stepWhenEnteredTitanOrbit = Integer.MAX_VALUE;
    int stepsNeededForOneOrbit = 159;
//    int stepsNeededForOneOrbit = Integer.MAX_VALUE;
    boolean printed = false;
    boolean slow = false;
    boolean log = false;

    private Vector startingFire_1 = new Vector(new double[]{19.348420398309827, -7.455923667177558, -0.8260710537433624});
    private Vector startingFire_2 = new Vector(new double[]{19.377969997003675, -7.467248549684882, -0.5318658649921417});

//    private Vector returningFire_1_AND_2 = new Vector(new double[]{-19.348420398309827, 7.455923667177558, 0.8260710537433624});
    private Vector returningFire_1_AND_2 = new Vector(new double[]{-19.348420398309827, 7.455923667177558, 2.9588835537433624});

    public FlightControlsTwoEngineFiresForLaunch_Exp(Vector returningFire_1_AND_2) {
        this.returningFire_1_AND_2 = returningFire_1_AND_2;
    }

    public FlightControlsTwoEngineFiresForLaunch_Exp() {}

    @Override
    public void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize) {

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
