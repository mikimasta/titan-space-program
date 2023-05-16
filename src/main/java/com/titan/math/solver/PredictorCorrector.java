package com.titan.math.solver;

import com.titan.math.Vector;
import com.titan.math.function.Function;

public class PredictorCorrector implements Solver {

    private final double stepSize;
    private Vector[] previousState;
    private Vector[] currentState = new Vector[2];
    private boolean isFirstIteration = true;

    public PredictorCorrector(double stepSize, Vector[] previousState, double stepSize1, Vector[] previousState1) {

        this.stepSize = stepSize1;
        this.previousState = previousState1;
    }

    private Vector[] bootstrap(Function f, Vector positions, Vector velocities, Vector masses, int t) {

        Solver rungeKutta = new RungeKuttaSolver(stepSize);

        isFirstIteration = false;

        return rungeKutta.solve(f, positions, velocities, masses, t);
    }


    @Override
    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, int t) {

        if (isFirstIteration) {
            currentState = bootstrap(f, previousState[0], previousState[1], masses, t);
            return currentState;
        }

        return new Vector[0];
    }

}
