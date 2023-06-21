package com.titan;

import com.titan.controls.Hurricane;
import com.titan.controls.LandingControls;
import com.titan.controls.Wind;
import com.titan.math.Vector;
import com.titan.math.function.Function;
import com.titan.math.function.LandingGravitationFunction;
import com.titan.math.solver.Solver;
import com.titan.model.LandingModule;
import com.titan.model.Rocket;

/**
 * Runs the simulation according to the specified solver and controls
 */
public class LandingSimulation {

    private final Solver solver;
    private final Wind wind;
    private final int stepSize;
    private final LandingModule module;
    private final LandingControls controls;
    private final Function landingGravitationFunction;

    public LandingSimulation(Solver solver, int stepSize, LandingModule module, LandingControls controls) {
        this.solver = solver;
        this.stepSize = stepSize;
        this.module = module;
        this.wind = new Hurricane();
        this.controls = controls;
        this.landingGravitationFunction = new LandingGravitationFunction();
    }

    public void nextStep(int currentStep) {
         controls.execute(module, currentStep);
         Vector[] nextState = solver.solve(
                landingGravitationFunction,
                 module.getPosition(),
                 module.getVelocity(),
                 new Vector(new double[] {0,0}),
                 stepSize,
                 currentStep);
         module.updatePosition(nextState[0]);
         module.updateVelocity(nextState[1]);
         wind.blow(module, stepSize);
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
