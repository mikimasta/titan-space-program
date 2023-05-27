package com.titan.experiments;

import com.titan.model.Rocket;
import com.titan.Simulation;
import com.titan.math.Vector;
import com.titan.math.solver.AdamsBashforth2ndOrderSolver;
import com.titan.model.SolarSystem;
import com.titan.math.solver.Solver;

public class CalculateInitialVelocity {
/*    
    private static final int STEP_SIZE = 60;

    private static final int ONE_YEAR_IN_SECONDS = 31536000;

    private CalculateInitialVelocity() {}



    private static void runForAYear(Simulation simulation) {
        System.out.print(" >>");
        for (int i = 0; i < ((ONE_YEAR_IN_SECONDS) / STEP_SIZE); i++) {
            simulation.nextStep(i);
            if (i % (ONE_YEAR_IN_SECONDS / STEP_SIZE / 10) == 0) {
                System.out.print(i/(60*60*24*356 / STEP_SIZE / 10));
            }
        }
        System.out.println("\nSimulation finished");
    }

    private static Simulation createSimulation() {
        
        SolarSystem system = new SolarSystem();
//        Rocket experia = system.createRocket("Experia", 50000);

     system.stageRocket(experia);

        Solver solver = new AdamsBashforth2ndOrderSolver(STEP_SIZE);

        return new Simulation(solver, STEP_SIZE, system);
    }
    
    private static Vector titanPosAfterOneYear(Simulation sim) {
        runForAYear(sim);
        
 //       Vector titanPos = sim.getSystem().getPosition("Titan");

        sim = createSimulation();

        return titanPos;
    }

    private static void calculateInitialVelocity(Simulation sim) {
        
        Vector rocketPosition = sim.getSystem().getPosition("Experia");

        Vector titanPosition = titanPosAfterOneYear(sim);

        System.out.println(titanPosition);

        System.out.println(titanPosition.subtract(rocketPosition).multiplyByScalar(1.0d/ONE_YEAR_IN_SECONDS));
    }


    public static void main(String[] args) {
        
        Simulation sim = createSimulation();



        calculateInitialVelocity(sim);
    }
    */
}

