package com.titan.experiments;

import com.titan.LandingSimulation;
import com.titan.controls.FourthLandingControls;
import com.titan.controls.LandingControls;
import com.titan.controls.Wind;
import com.titan.controls.Wind.WindType;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.model.LandingModule;

import java.util.ArrayList;

/**
 * class that helps calculate the correct velocity of the probe for it to hit titan
 */
public class WindExperiments {

    static ArrayList<Double> distances = new ArrayList<>();
    static ArrayList<Double> velocities = new ArrayList<>();

    public static void main(String[] args) {

        for (int i = 0; i < 100; ++i) {
                
            System.out.println(i);
            simulate(new Wind(WindType.HURRICANE));
        
        }

        distances.sort(Double::compareTo);
        System.out.println("Min dist: " + distances.get(0));
        System.out.println("Max dist: " + distances.get(distances.size() - 1));
        System.out.println("Avg dist: " + distances.stream()
                .mapToDouble(it -> it)
                .sum() / distances.size());


        velocities.sort(Double::compareTo);
        System.out.println("Min vel: " + velocities.get(0));
        System.out.println("Max vel: " + velocities.get(velocities.size() - 1));
        System.out.println("Avg vel: " + velocities.stream()
                .mapToDouble(it -> it)
                .sum() / velocities.size());

        System.out.println("successful landings(8km/h): " + velocities.stream()
                .filter(it -> it <= 8d/3600d)
                .count());
        System.out.println("----");
        System.out.println("successful landings(20km/h): " + velocities.stream()
                .filter(it -> it <= 20d/3600d)
                .count());
        

    }

    static void simulate(Wind wind) {

        RungeKuttaSolver solver = new RungeKuttaSolver();
        LandingModule module = new LandingModule("Landing Module");
        LandingControls controls = new FourthLandingControls();
        double initialStepSize = 1;
        LandingSimulation simulation = new LandingSimulation(solver, initialStepSize, module, controls, wind);

        while (module.getY() > 0) {
            simulation.nextStep(1);
        }
        distances.add(Math.abs(module.getX()));
        velocities.add(module.getTotalSpeed());
    }

}
