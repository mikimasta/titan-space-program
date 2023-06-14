package com.titan.experiments;

import com.titan.math.Vector;
import com.titan.math.function.Function;
import com.titan.math.solver.AdamsBashforth2ndOrderSolver;
import com.titan.math.solver.EulerSolver;
import com.titan.math.solver.PredictorCorrector;
import com.titan.math.solver.RungeKuttaSolver;

public class SolverStepSizeExperiments {

    /**
     * test the solvers with the following problem: <p>
     * y' = f(y, t) = (t + y)^2 -1 <br>
     * up to t = 1 with y(0) = 2/3 <br>
     * the actual solution is y(t)=2/3(-2t), <br>
     * hence the expected value at t=1 is 1. <p>
     * h varies over the experiment to see how the error is evolving
     */
    public static void main(String[] args) {
        Function f = (v1, v2, v3, h, t) -> new Vector(new double[]{Math.pow(v2.getValue(0) + t, 2) - 1});

        for (double j = 0; j < 20; j++) {
            double h = 1.0/Math.pow(2, j);
            Vector y = new Vector(new double[]{2.0/3});
            EulerSolver eulerSolver = new EulerSolver();
            AdamsBashforth2ndOrderSolver adamsBashforth2ndOrderSolver = new AdamsBashforth2ndOrderSolver();
            RungeKuttaSolver rungeKuttaSolver = new RungeKuttaSolver();
            PredictorCorrector predictorCorrector = new PredictorCorrector();
            for (int i = 0; i < 1/h; i++) {
                Vector[] result = predictorCorrector.solve(f, new Vector(new double[]{0}), y, new Vector(new double[]{}), h, i*h);
                y = result[1];
            }
            System.out.println("h = " + h + ", y: " + y.getValue(0) + "");
        }
    }
}
