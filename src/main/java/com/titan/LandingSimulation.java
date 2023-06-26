package com.titan;

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
    private double stepSize;
    private final LandingModule module;
    private final LandingControls controls;
    private final Function landingGravitationFunction;
    private double currentTime = 0;

    public LandingSimulation(Solver solver, double stepSize, LandingModule module, LandingControls controls, Wind wind) {
        this.solver = solver;
        this.stepSize = stepSize;
        this.module = module;
        this.wind = wind;
        this.controls = controls;
        this.landingGravitationFunction = new LandingGravitationFunction();
    }

    public void nextStep(int currentStep) {
         controls.execute(module, currentStep, stepSize);
         Vector[] nextState = solver.solve(
                 landingGravitationFunction,
                 module.getPosition(),
                 module.getVelocity(),
                 module.getThrust(),
                 stepSize,
                 currentStep);
         module.updatePosition(nextState[0]);
         module.updateVelocity(nextState[1]);
         wind.blow(module, stepSize, currentTime);
         currentTime += stepSize;
         updateStepSize();
    }

    private void updateStepSize() {
        stepSize = 1;
        if (module.getY() < 10) stepSize = 0.1;
        if (module.getY() < 1) stepSize = 0.01;
        if (module.getY() < 0.1) stepSize = 0.001;
        if (module.getY() < 0.01) stepSize = 0.0001;
        if (module.getY() < 0.001) stepSize = 0.00001;
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

    public Wind getWind() {
        return wind;
    }

    public double getStepSize() {
        return stepSize;
    }

}
