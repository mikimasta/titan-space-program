package com.titan.experiments;

import com.titan.Simulation;
import com.titan.math.solver.PredictorCorrector;
import com.titan.math.solver.Solver;
import com.titan.model.CelestialObject;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

import java.util.ArrayList;

/**
 * class that helps calculate the runtime of the program (without GUI)
 */
public class CalculateRuntime {

    /**
     * step we are currently on
     */
    private static int currentStep;


    private static int  launchNumber = 1;

    /**
     * here the velocity is updated with every iteration of the complete solution of the solar system after a year. the distance to Titan on the 1st of April 2024 is calculated and
     * velocity is being changed accordingly
     * @param args
     */
    public static void main(String[] args) {

        final long startTime = System.nanoTime();


            SolarSystem s = new SolarSystem();
            Rocket rocket = s.createRocketOnEarth("Experia " + launchNumber, 50000);
            s.stageRocket(rocket);
            ArrayList<CelestialObject> obj = s.getCelestialObjects();
            currentStep = 0;

            Solver solver = new PredictorCorrector();
            Simulation simulation = new Simulation(solver, 60, s);

            while (currentStep < 365 * 24 * 60) {
                for (int i = 0; i < 1; i++) {
                    simulation.nextStep(currentStep);
                    currentStep++;
                }
            }

        final long totalTime = System.nanoTime() - startTime;
        System.out.println(totalTime);
    }
}

