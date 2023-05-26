package com.titan.experiments;

import com.titan.Simulation;
import com.titan.controls.Controls;
import com.titan.controls.FlightControlsTwoEngineFiresForLaunch;
import com.titan.gui.Titan;
import com.titan.math.Vector;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.math.solver.Solver;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

import java.util.List;

public class CalculateReturningVelocity_HillClimbing {

    static int stepSize = 60;
    static final int ONE_YEAR_IN_SECONDS = 31536000;

    private static void runForTwoYear(Simulation simulation) {
        System.out.print(" >>");
        for (int i = 0; i < ((ONE_YEAR_IN_SECONDS)*2 / stepSize); i++) {
            simulation.nextStep(i);
            if (i % (ONE_YEAR_IN_SECONDS / stepSize / 10) == 0) {
                System.out.print(i/(ONE_YEAR_IN_SECONDS / stepSize / 10));
            }
        }
    }

    private static Simulation setUpSimulation(Vector returningVelocityOfRocket) {
        SolarSystem system = new SolarSystem();
        Rocket rocket = system.createRocketOnEarth(
                "Rocket",
                50000);
        system.stageRocket(rocket);

        Solver solver = new RungeKuttaSolver(stepSize);
        Controls controls = new FlightControlsTwoEngineFiresForLaunch(returningVelocityOfRocket);

        return new Simulation(solver, stepSize, controls, system, rocket);
    }

    private static List<Object> climbTheHill(Vector returningVelocity, double startingDistance, double climbingStepSize) {
        List<Object> result = List.of(returningVelocity, startingDistance);

        boolean complete = false;
        while (!complete) {
            complete = true;

            // z
            result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, 0, 0.5}), climbingStepSize);
            if (result.get(0).equals(returningVelocity)) {
                result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, 0, -0.5}), climbingStepSize);
            }
            if (!result.get(0).equals(returningVelocity)) {
                complete = false;
                returningVelocity = (Vector) result.get(0);
                continue;
            }

            // x
            result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{1, 0, 0}), climbingStepSize);
            if (result.get(0).equals(returningVelocity)) {
                result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{-1, 0, 0}), climbingStepSize);
            }
            if (!result.get(0).equals(returningVelocity)) {
                complete = false;
                returningVelocity = (Vector) result.get(0);
            }

            // y
            result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, 1, 0}), climbingStepSize);
            if (result.get(0).equals(returningVelocity)) {
                result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, -1, 0}), climbingStepSize);
            }
            if (!result.get(0).equals(returningVelocity)) {
                complete = false;
                returningVelocity = (Vector) result.get(0);
            }
        }

        System.out.println("# current best: " + result.get(1) + " with velocity: " + result.get(0));
        return List.of(returningVelocity, result.get(1));
    }

    private static List<Object> climbOnceInOneDirection(Vector returningVelocity, double distanceToEarth, Vector climbingDirection, double climbingStepSize) {
        Vector newReturningVelocity = returningVelocity.add(climbingDirection.multiplyByScalar(climbingStepSize));
        System.out.print("Test with velocity: " + newReturningVelocity);
        Simulation simulation = setUpSimulation(newReturningVelocity);
        runForTwoYear(simulation);
        //runForAYear(simulation);
        System.out.println();
        if (getDistanceToEarth(simulation) < distanceToEarth) {
            distanceToEarth = getDistanceToEarth(simulation);
            returningVelocity = newReturningVelocity;
            System.out.println("new best: " + (int) distanceToEarth);
        }
        return List.of(returningVelocity, distanceToEarth);
    }

    private static double getDistanceToEarth(Simulation simulation) {
        SolarSystem system = simulation.getSystem();
        return system.getCelestialObjects().get(system.getCelestialObjects().size()-1).getPosition().subtract(system.getCelestialObjects().get(3).getPosition()).getLength();
    }


    public static void main(String[] args) {
        Vector initialVelocity =  new Vector(new double[]{-26.586884753778577 ,-0.9415220115333796 ,0.9692137539386749});
        Titan.currentStep = 1; // to avoid out of memory bc of historic positions/velocities
        Simulation simulation = setUpSimulation(initialVelocity);
        runForTwoYear(simulation);
        System.out.println();
        List<Object> result = List.of(initialVelocity, getDistanceToEarth(simulation));

        for (int i = 2; i < 30; i++) {
            double climbingStepSize = 10.0/Math.pow(2, i);
            System.out.println("next climbing step size: " + climbingStepSize + " (10 / 2^" + i + ")");
            result = climbTheHill((Vector) result.get(0), (Double) result.get(1), climbingStepSize);
        }

        Simulation simulation2 = setUpSimulation((Vector) result.get(0));

        runForTwoYear(simulation2);

        System.out.println("Distance Rocket - Earth: " + (int) getDistanceToEarth(simulation2) + " km");
    }
}
