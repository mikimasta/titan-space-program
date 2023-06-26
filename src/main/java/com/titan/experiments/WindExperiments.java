package com.titan.experiments;

import com.titan.LandingSimulation;
import com.titan.controls.FourthLandingControls;
import com.titan.controls.LandingControls;
import com.titan.controls.Wind;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.model.LandingModule;

import java.util.Locale;

/**
 * class that helps calculate the correct velocity of the probe for it to hit titan
 */
public class WindExperiments {

    public static void main(String[] args) {

        RungeKuttaSolver solver = new RungeKuttaSolver();
        LandingModule module = new LandingModule("Landing Module");
        LandingControls controls = new FourthLandingControls();
        double initialStepSize = 1;
        Wind wind = new Wind(Wind.WindType.HURRICANE);
        LandingSimulation simulation = new LandingSimulation(solver, initialStepSize, module, controls, wind);

        while (module.getY() > 0) {
            simulation.nextStep(1);
        }
        System.out.printf(
                Locale.ENGLISH,
                "Landed at x = %.6f km, y = %.6f km, x' = %.6f km/s, y' = %.6f km/s, angle = %.2f°\n",
                module.getX(),
                module.getY(),
                module.getVelocity().getValue(0),
                module.getVelocity().getValue(1),
                module.getRotationAngle());
    }

}
