package com.titan.math;

/**
 * Two-stage Adam Bashforth implements Solver: <br>
 * implementation of the Euler-Solver approximation method <br>
 * wi+1 = wi + (h/2)(3f (ti, wi) − f (ti−1, wi−1))
 */

public class TwoStageAdamBashforth implements Solver {

    /**
     * determines the step size for the Adam Bashforth solver
     */
    private final double stepSize;

    /**
     * this method calculates the positions and velocities of all celestial objects after every iteration using the Adam Bashforth method
     * @param positions vector of all the positions
     * @param velocities vector of all the velocities
     * @param t step we are currently on
     */

    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, int t) {

        //solver

        return result;
    }

}
