package com.titan;

import com.titan.controls.Controls;
import com.titan.math.Vector;
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

    public Simulation(Solver solver, int stepSize, Controls controls, SolarSystem system, Rocket rocket) {
        this.solver = solver;
        this.stepSize = stepSize;
        this.controls = controls;
        this.system = system;
        this.rocket = rocket;
    }

    public Simulation(Solver solver, int stepSize, SolarSystem system) {
        this.solver = solver;
        this.stepSize = stepSize;
        this.controls = null;
        this.system = system;
        this.rocket = null;
    }


    public void nextStep(int currentStep) {
        if (controls != null) controls.execute(system, rocket, currentStep, stepSize);
        Vector[] nextState = solver.solve(
                new GravitationFunction(),
                system.getAllPositions(),
                system.getAllVelocities(),
                system.getAllMasses(),
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

    public void runForAYear() {
        runForAYear(this);
    }

    public void runForAYear(boolean printProgress) {
        runForAYear(this, printProgress);
    }

    public static void runForAYear(Simulation simulation, boolean printProgress) {
        int oneYearInSeconds = 31536000;
        if (printProgress) System.out.print(" >>");
        for (int i = 0; i < ((oneYearInSeconds) / simulation.stepSize); i++) {
            simulation.nextStep(i);
            if (printProgress && i % (oneYearInSeconds / simulation.stepSize / 10) == 0) {
                System.out.print(i / (oneYearInSeconds / simulation.stepSize / 10));
            }
        }
    }

    public static void runForAYear(Simulation simulation) {
        runForAYear(simulation, false);
    }
}
