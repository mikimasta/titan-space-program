package com.titan.math.solver;

import com.titan.math.DifferentialEquation;
import com.titan.math.Vector;
import com.titan.math.function.Function;

/**
 * Implementation of the Predictor-Corrector approach solver approximation method <br>
 *
 * wi+1 = wi + (h/12)*(5*f(t(i+1), w(i+1)) + 8*f(t(i), w(i)) - f(t(i-1), w(i-1)))
 *
 */
public class PredictorCorrector implements Solver {

    /**
     * keeps track of the previous state of the system, needed for calculations using this method
     */
    private final Vector[] previousState = new Vector[2];

    /**
     * a flag that helps distinguish whether a bootstrap is needed to start the method
     */
    private boolean isFirstIteration = true;

    /**
     * bootstraps the first step to get the solver running
     * @param f gravitational function acting upon the celestial bodies
     * @param positions vector of positions of the entire system
     * @param velocities vector of velocities of the entire system
     * @param masses vector of masses of the entire system
     * @param t step the system is currently on
     * @return returns the state of the system after the initial step using a Runge-Kutta 4th order method
     */
    private Vector[] bootstrap(Function f, Vector positions, Vector velocities, Vector masses, double h, double t) {
        isFirstIteration = false;
        previousState[0] = positions;
        previousState[1] = velocities;
        return (new RungeKuttaSolver()).solve(f, positions, velocities, masses, h, t);
    }

    /**
     * takes one step forward and calculates the state of the solar system using the Predictor-Corrector approach method
     * @param f {@link Function} that is used to estimate the next step
     * @param positions positions of celestial bodies in the system
     * @param velocities velocities of celestial bodies in the system
     * @param masses masses of celestial bodies in the system
     * @param t time step we are currently on
     * @return returns the state of the system after one step
     */
    @Override
    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, double h, double t) {

        if (isFirstIteration) {
            return bootstrap(f, positions, velocities, masses, h, t);
        }

        Vector[] diffCurrent = DifferentialEquation.solve(f, positions, velocities, masses, h, t);
        Vector[] diffPrevious = DifferentialEquation.solve(f, previousState[0], previousState[1], masses, h, t-h);

        AdamsBashforth2ndOrderSolver adamsBashforth2ndOrderSolver = new AdamsBashforth2ndOrderSolver();
        adamsBashforth2ndOrderSolver.setIsFirstIteration(false);
        adamsBashforth2ndOrderSolver.setPreviousState(previousState);
        Vector[] nextStateApprox = adamsBashforth2ndOrderSolver.solve(f, positions, velocities, masses, h,t+h);

        Vector[] diffNextStateApprox = DifferentialEquation.solve(f, nextStateApprox[0], nextStateApprox[1], masses, h,t+h);

        Vector[] nextState = new Vector[2];

        Vector nextPosition = positions.add((diffCurrent[0].multiplyByScalar(8))
                .add(diffNextStateApprox[0].multiplyByScalar(5))
                .subtract(diffPrevious[0])
                .multiplyByScalar(h/12));

        Vector nextVelocity = velocities.add((diffCurrent[1].multiplyByScalar(8))
                .add(diffNextStateApprox[1].multiplyByScalar(5))
                .subtract(diffPrevious[1])
                .multiplyByScalar(h/12));


        nextState[0] = nextPosition;
        nextState[1] = nextVelocity;

        previousState[0] = positions;
        previousState[1] = velocities;

        return nextState;

    }

}
