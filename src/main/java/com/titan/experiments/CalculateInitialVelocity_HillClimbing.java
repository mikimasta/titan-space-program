package com.titan.experiments;

import com.titan.Simulation;
import com.titan.gui.Titan;
import com.titan.math.Vector;
import com.titan.math.solver.EulerSolver;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.math.solver.Solver;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

import java.util.List;

public class CalculateInitialVelocity_HillClimbing {

    static int stepSize = 60;
    static final int ONE_YEAR_IN_SECONDS = 31536000;

    private static void runForAYear(Simulation simulation) {
        System.out.print(" >>");
        for (int i = 0; i < ((ONE_YEAR_IN_SECONDS) / stepSize); i++) {
            simulation.nextStep(i);
            if (i % (ONE_YEAR_IN_SECONDS / stepSize / 10) == 0) {
                System.out.print(i/(60*60*24*356 / stepSize / 10));
            }
        }
    }

    private static Simulation setUpSimulation(Vector initialVelocityOfRocket) {
        SolarSystem system = new SolarSystem();
        Rocket rocket = system.createRocket("Rocket", 50000);
        system.stageRocket(rocket);
        rocket.fireEngineWithVelocity(initialVelocityOfRocket, stepSize);

        Solver solver = new RungeKuttaSolver(stepSize);
        return new Simulation(solver, stepSize, system);
    }

    private static List<Object> climbTheHill(Vector startingVelocity, double startingDistance, double climbingStepSize) {
        List<Object> result = List.of(startingVelocity, startingDistance);

        boolean complete = false;
        while (!complete) {
            complete = true;

            // x
            result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{1, 0, 0}), climbingStepSize);
            if (result.get(0).equals(startingVelocity)) {
                result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{-1, 0, 0}), climbingStepSize);
            }
            if (!result.get(0).equals(startingVelocity)) {
                complete = false;
                startingVelocity = (Vector) result.get(0);
            }

            // y
            result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, 1, 0}), climbingStepSize);
            if (result.get(0).equals(startingVelocity)) {
                result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, -1, 0}), climbingStepSize);
            }
            if (!result.get(0).equals(startingVelocity)) {
                complete = false;
                startingVelocity = (Vector) result.get(0);
            }

            // z
            result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, 0, 0.1}), climbingStepSize);
            if (result.get(0).equals(startingVelocity)) {
                result = climbOnceInOneDirection((Vector) result.get(0), (Double) result.get(1), new Vector(new double[]{0, 0, -0.1}), climbingStepSize);
            }
            if (!result.get(0).equals(startingVelocity)) {
                complete = false;
                startingVelocity = (Vector) result.get(0);
            }
        }

        System.out.println("# current best: " + result.get(1) + " with velocity: " + result.get(0));
        return List.of(startingVelocity, result.get(1));
    }

    private static List<Object> climbOnceInOneDirection(Vector startingVelocity, double distanceToTitan, Vector climbingDirection, double climbingStepSize) {
        Vector newStartingVelocity = startingVelocity.add(climbingDirection.multiplyByScalar(climbingStepSize));
        System.out.print("Test with velocity: " + newStartingVelocity);
        Simulation simulation = setUpSimulation(newStartingVelocity);
        runForAYear(simulation);
        System.out.println();
        if (getDistanceToTitan(simulation) < distanceToTitan) {
            distanceToTitan = getDistanceToTitan(simulation);
            startingVelocity = newStartingVelocity;
            System.out.println("new best: " + (int) distanceToTitan);
        }
        return List.of(startingVelocity, distanceToTitan);
    }

    private static double getDistanceToTitan(Simulation simulation) {
        SolarSystem system = simulation.getSystem();
        return system.getCelestialObjects().get(system.getCelestialObjects().size()-1).getPosition().subtract(system.getTitan().getPosition()).getLength();
    }


    public static void main(String[] args) {
        Vector initialVelocity = new Vector(new double[]{35, -14, -1});
        Titan.currentStep = 1; // to avoid out of memory bc of historic positions/velocities
        Simulation simulation = setUpSimulation(initialVelocity);
        runForAYear(simulation);
        System.out.println();
        List<Object> result = List.of(initialVelocity, getDistanceToTitan(simulation));

        for (int i = 0; i < 30; i++) {
            double climbingStepSize = 10.0/Math.pow(2, i);
            System.out.println("next climbing step size: " + climbingStepSize + " (10 / 2^" + i + ")");
            result = climbTheHill((Vector) result.get(0), (Double) result.get(1), climbingStepSize);
        }

        Simulation simulation2 = setUpSimulation((Vector) result.get(0));

        runForAYear(simulation2);

        System.out.println("Distance Rocket - Titan: " + (int) getDistanceToTitan(simulation2) + " km");
    }
}
