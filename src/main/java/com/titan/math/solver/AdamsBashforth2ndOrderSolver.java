package com.titan.math.solver;


import com.titan.math.DifferentialEquation;
import com.titan.math.Vector;
import com.titan.math.function.Function;

/**
 * Implementation of the Adams-Bashforth 2nd order solver approximation method <br>
 *
 * wi+1 = wi + (h/2)*(3*f(ti, wi) - f(t-1, w-1))
 *
 */
public class AdamsBashforth2ndOrderSolver implements Solver {

    /**
     * determines the step size for Adams-Bashforth2
     */
    private final double stepSize;

    /**
     * keeps track of the previous state of the system, needed for calculations using this method
     */
    private Vector[] previousState;

    /**
     * a flag that helps distinguish whether a bootstrap is needed to start the method
     */
    private boolean isFirstIteration = true;

    /**
     * stores the current state of the system
     */
    private Vector[] currentState = new Vector[2];

    /**
     * creates a new Adams-Bashforth solver instance; sets the step size and the initial state of the system
     * @param stepSize
     * @param previousState previous, or rather initial state of the entire system in this case
     */
    public AdamsBashforth2ndOrderSolver(double stepSize, Vector[] previousState) {
        this.stepSize = stepSize;
        this.previousState = previousState;
    }


    /**
     * bootstraps the first step to get the solver running
     * @param f gravitational function acting upon the celestial bodies
     * @param positions vector of posistions of the entire system
     * @param velocities vector of velocitites of the entire system
     * @param masses vector of masses of the entire system
     * @param t step the system is currently on
     * @return returns the state of the system after the initial step using a Runge-Kutta 4th order method
     */
    private Vector[] bootstrap(Function f, Vector positions, Vector velocities, Vector masses, int t) {

        Solver rungeKutta = new RungeKuttaSolver(stepSize);

        isFirstIteration = false;

        return rungeKutta.solve(f, positions, velocities, masses, t);
    }

    /**
     * takes one step forward and calculates the state of the solar system using the Adams-Bashforth 2nd order method
     * @param f {@link Function} that is used to estimate the next step
     * @param positions positions of celestial bodies in the system
     * @param velocities velocities of celestial bodies in the system
     * @param masses masses of celestial bodies in the system
     * @param t time step we are currently on
     * @return returns the state of the system after one step
     */
    @Override
    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, int t) {


        if (isFirstIteration) {
            currentState = bootstrap(f, previousState[0], previousState[1], masses, t);
            return currentState;
        }

        Vector[] nextState = new Vector[2];

        Vector[] diffCurrent = DifferentialEquation.solve(f, currentState[0], currentState[1], masses);
        Vector[] diffPrevious = DifferentialEquation.solve(f, previousState[0], previousState[1], masses);

        Vector nextPos = currentState[0]
                .add(diffCurrent[0]
                        .multiplyByScalar(3)
                        .subtract(diffPrevious[0])
                        .multiplyByScalar(stepSize / 2));

        Vector nextVel = currentState[1]
                .add(diffCurrent[1]
                        .multiplyByScalar(3)
                        .subtract(diffPrevious[1])
                        .multiplyByScalar(stepSize / 2));

        nextState[0] = nextPos;
        nextState[1] = nextVel;

        previousState = currentState.clone();
        currentState = nextState.clone();


        return nextState;
    }
}
