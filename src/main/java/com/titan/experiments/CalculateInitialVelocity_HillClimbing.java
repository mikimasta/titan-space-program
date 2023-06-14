package com.titan.experiments;

import com.titan.Simulation;
import com.titan.controls.Controls;
import com.titan.controls.SecondTestControls;
import com.titan.math.Vector;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.math.solver.Solver;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

public class CalculateInitialVelocity_HillClimbing extends HillClimbing {

    @Override
    Simulation setUpSimulation(Vector input, int stepSize) {
        SolarSystem system = new SolarSystem();
        Rocket rocket = system.createRocketOnEarth("Rocket", 50000);
        system.stageRocket(rocket);

        Solver solver = new RungeKuttaSolver();
        Controls controls = new SecondTestControls(input);

        return new Simulation(solver, stepSize, controls, system, rocket);
    }

    @Override
    double calculateError(Simulation simulation) {
        SolarSystem system = simulation.getSystem();
        return system.getCelestialObjects()
                .get(system.getCelestialObjects().size()-1)
                .getPosition()
                .subtract(system.getTitan().getPosition()).getLength();
    }



    public static void main(String[] args) {
        Vector initialVelocity = new Vector(new double[]{38.696840796619654, -14.911847334355116, -1.6521421074867249}).multiplyByScalar(0.5);

        CalculateInitialVelocity_HillClimbing climber = new CalculateInitialVelocity_HillClimbing();
        Result result = climber.run(initialVelocity, 60, 10);
        System.out.println("Final result: velocity = " + result.input + "; distance = " + result.output);
    }
}
