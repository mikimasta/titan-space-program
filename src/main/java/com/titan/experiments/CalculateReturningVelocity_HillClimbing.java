package com.titan.experiments;

import com.titan.Simulation;
import com.titan.controls.Controls;
import com.titan.controls.FlightControlsTwoEngineFiresForLaunch;
import com.titan.math.Vector;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.math.solver.Solver;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

public class CalculateReturningVelocity_HillClimbing extends HillClimbing {

    public CalculateReturningVelocity_HillClimbing() {
        secondsUntilResultsAreCompared = 31536000 * 2; // two years
    }

    @Override
    Simulation setUpSimulation(Vector input, int stepSize) {
        SolarSystem system = new SolarSystem();
        Rocket rocket = system.createRocketOnEarth(
                "Rocket",
                50000);
        system.stageRocket(rocket);

        Solver solver = new RungeKuttaSolver();
        Controls controls = new FlightControlsTwoEngineFiresForLaunch(input);

        return new Simulation(solver, stepSize, controls, system, rocket);
    }

    @Override
    double calculateError(Simulation simulation) {
        SolarSystem system = simulation.getSystem();
        return system.getCelestialObjects().get(system.getCelestialObjects().size()-1).getPosition().subtract(system.getCelestialObjects().get(3).getPosition()).getLength();
    }

    public static void main(String[] args) {
        Vector initialVelocity =  new Vector(new double[]{-26.586884753778577 ,-0.9415220115333796 ,0.9692137539386749});

        CalculateReturningVelocity_HillClimbing climber = new CalculateReturningVelocity_HillClimbing();
        Result result = climber.run(initialVelocity, 60, 10);
        System.out.println("Final result: velocity = " + result.input + "; distance = " + result.output);
    }
}
