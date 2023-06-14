package com.titan;

import com.titan.math.solver.Solver;
import com.titan.model.LandingModule;
import com.titan.model.Rocket;

/**
 * Runs the simulation according to the specified solver and controls
 */
public class LandingSimulation {

    private final Solver solver;
    private final int stepSize;
    private final LandingModule module;

    public LandingSimulation(Solver solver, int stepSize, LandingModule module) {
        this.solver = solver;
        this.stepSize = stepSize;
        this.module = module;
    }

    public void nextStep(int currentStep) {
        // Vector[] nextState = solver.solve(
        //         new GravitationFunction(),
        //         module.getPosition(),
        //         module.getVelocity(),
        //         new Vector(new double[] {module.getM()}),
        //         stepSize,
        //         currentStep);
        // module.updatePosition(nextState[0]);
        // module.updateVelocity(nextState[1]);
    }

    public Rocket getModule() {
        return module;
    }

    public void runFor(int seconds) {
        runFor(this, seconds, false);
    }

    public void runFor(int seconds, boolean printProgress) {
        runFor(this, seconds, printProgress);
    }


    public static void runFor(LandingSimulation simulation, int seconds, boolean printProgress) {
        if (printProgress) System.out.print(" >>");
        for (int i = 0; i < ((seconds) / simulation.stepSize); i++) {
            simulation.nextStep(i);
            if (printProgress && i % (seconds / simulation.stepSize / 10) == 0) {
                System.out.print(i / (seconds / simulation.stepSize / 10));
            }
        }
    }
}
