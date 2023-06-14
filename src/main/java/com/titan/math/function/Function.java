package com.titan.math.function;

import com.titan.math.Vector;
import com.titan.math.solver.Solver;

/**
 * Functional interface, represents a function that operates on three vectors
 */
public interface Function {

    /**
     *
     * @param v1 positions (used like this in the {@link Solver} instances of the program)
     * @param v2 velocities (used like this in the {@link Solver} instances of the program)
     * @param v3 masses (used like this in the {@link Solver} instances of the program)
     * @return resulting Vector of the function
     */
    Vector f(Vector v1, Vector v2, Vector v3, double h, double t);
}
