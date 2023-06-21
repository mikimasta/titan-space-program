package com.titan;

import com.titan.controls.Controls;
import com.titan.math.Vector;
import com.titan.math.function.Function;
import com.titan.math.function.GravitationFunction;
import com.titan.math.solver.Solver;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

/**
 * Runs the simulation according to the specified solver and controls
 */
public class Simulation {

    private final Solver solver;
    private final int stepSize;
    private final Controls controls;
    private final SolarSystem system;
    private final Rocket rocket;
    private final Function gravitationFunction;

    public Simulation(Solver solver, int stepSize, Controls controls, SolarSystem system, Rocket rocket) {
        this.solver = solver;
        this.stepSize = stepSize;
        this.controls = controls;
        this.system = system;
        this.rocket = rocket;
        gravitationFunction = new GravitationFunction();
    }

    public Simulation(Solver solver, int stepSize, SolarSystem system) {
        this.solver = solver;
        this.stepSize = stepSize;
        this.controls = null;
        this.system = system;
        this.rocket = null;
        gravitationFunction = new GravitationFunction();
    }


    public void nextStep(int currentStep) {
        if (controls != null) controls.execute(system, rocket, currentStep, stepSize);
        Vector[] nextState = solver.solve(
                gravitationFunction,
                system.getAllPositions(),
                system.getAllVelocities(),
                system.getAllMasses(),
                stepSize,
                currentStep);
        system.setAllPositions(nextState[0]);
        system.setAllVelocities(nextState[1]);
    }

    public SolarSystem getSystem() {
        return system;
    }

    public Rocket getRocket() {
        return rocket;
    }

    public void runFor(int seconds) {
        runFor(this, seconds, false);
    }

    public void runFor(int seconds, boolean printProgress) {
        runFor(this, seconds, printProgress);
    }

    public void runForAYear() {
        runForAYear(this, false);
    }

    public void runForAYear(boolean printProgress) {
        runForAYear(this, printProgress);
    }

    public static void runForAYear(Simulation simulation, boolean printProgress) {
        int oneYearInSeconds = 31536000;
        runFor(simulation, oneYearInSeconds, printProgress);
    }

    public static void runFor(Simulation simulation, int seconds, boolean printProgress) {
        if (printProgress) System.out.print(" >>");
        for (int i = 0; i < ((seconds) / simulation.stepSize); i++) {
            simulation.nextStep(i);
            if (printProgress && i % (seconds / simulation.stepSize / 10) == 0) {
                System.out.print(i / (seconds / simulation.stepSize / 10));
            }
        }
    }
}
