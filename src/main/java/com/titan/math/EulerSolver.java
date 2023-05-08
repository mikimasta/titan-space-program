package com.titan.math;

/**
 * EulerSolver implements Solver: <br>
 * implementation of the Euler-Solver approximation method <br>
 * wi+1 = wi + h * f(wi, ti)
 */
public class EulerSolver implements Solver {

    /**
     * determines the step size for the Euler solver
     */
    private final double stepSize;

    /**
     * Constructs an Euler solver and sets the step size
     * @param stepSize
     */
    public EulerSolver(double stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * this method calculates the positions and velocities of all celestial objects after every iteration using the explicit Euler method
     * @param positions vector of all the positions
     * @param velocities vector of all the velocities
     * @param t step we are currently on
     */
    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, int t) {
        Vector[] result = new Vector[2];

        result[0] = positions.add(velocities.multiplyByScalar(stepSize));
        result[1] = velocities.add(f.f(positions, velocities, masses).multiplyByScalar(stepSize));

        return result;
    }
}
