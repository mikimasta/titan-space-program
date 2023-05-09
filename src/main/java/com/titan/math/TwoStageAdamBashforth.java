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

    public TwoStageAdamBashforth(double stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * this method calculates the positions and velocities of all celestial objects after every iteration using the Adam Bashforth method
     * @param positions vector of all the positions
     * @param velocities vector of all the velocities
     * @param t step we are currently on
     */

    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, int t) {
        Vector[] result = new Vector[2];

        if (t == 0) {
            // For the first step, use the explicit Euler method.
            result[0] = positions.add(velocities.multiplyByScalar(stepSize));
            result[1] = velocities.add(f.f(positions, velocities, masses).multiplyByScalar(stepSize));
        } else {
            // Use the two-stage Adam-Bashforth method.
            Vector previousPositions = positions.subtract(velocities.multiplyByScalar(stepSize));
            Vector previousVelocities = velocities.subtract(f.f(previousPositions, velocities, masses).multiplyByScalar(stepSize));

            Vector stage1 = f.f(previousPositions, previousVelocities, masses);
            Vector stage2 = f.f(positions, velocities, masses);

            result[0] = positions.add(velocities.multiplyByScalar(stepSize * 1.5).subtract(previousVelocities.multiplyByScalar(stepSize * 0.5)).multiplyByScalar(0.5 / 3).multiplyByScalar(stepSize));
            result[1] = velocities.add(stage1.multiplyByScalar(1.5).subtract(stage2.multiplyByScalar(0.5)).multiplyByScalar(0.5 / 3).multiplyByScalar(stepSize));
        }
        System.out.println("Im here");
        return result;
    }

}
