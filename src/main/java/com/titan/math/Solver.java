package com.titan.math;

/**
 * A generic solver, solves differential equation based on Newton's law of motion
 */
public interface Solver {

    /**
     * solver for a differential equation
     *
     * @param f {@link Function} that is used to estimate the next step
     * @param v1 positions (used like this in the instances of this interface)
     * @param v2 velocities (used like this in the instances of this interface)
     * @param v3 masses (used like this in the instances of this interface)
     * @return resulting Vector of the function
     */
    Vector[] solve(Function f, Vector v1, Vector v2, Vector v3, int t);
}
