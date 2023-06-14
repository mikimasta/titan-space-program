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
     * @param h
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
     * sets the isFirstIteration variable to a specified boolean value
     * @param isFirstIteration boolean value for the isFirstIteration
     */
    public void setIsFirstIteration(boolean isFirstIteration){
        this.isFirstIteration = isFirstIteration;
    }

    /**
     * sets the previous state to a specified state vector
     * @param state a vector of the state of a celestial object, consisting its positions and velocities
     */
    public void setPreviousState(Vector[] state){
        previousState[0] = state[0];
        previousState[1] = state[1];

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
    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, double h, double t) {

        if (isFirstIteration) {
            return bootstrap(f, positions, velocities, masses, h, t);
        }

        Vector[] nextState = new Vector[2];

        Vector[] diffCurrent = DifferentialEquation.solve(f, positions, velocities, masses, h, t);
        Vector[] diffPrevious = DifferentialEquation.solve(f, previousState[0], previousState[1], masses, h, t-h);

        Vector nextPos = positions
                .add(diffCurrent[0]
                        .multiplyByScalar(3)
                        .subtract(diffPrevious[0])
                        .multiplyByScalar(h / 2));

        Vector nextVel = velocities
                .add(diffCurrent[1]
                        .multiplyByScalar(3)
                        .subtract(diffPrevious[1])
                        .multiplyByScalar(h / 2));

        nextState[0] = nextPos;
        nextState[1] = nextVel;

        previousState[0] = positions;
        previousState[1] = velocities;

        return nextState;
    }
}
