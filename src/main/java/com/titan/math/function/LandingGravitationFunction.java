package com.titan.math.function;

import com.titan.math.Vector;

public class LandingGravitationFunction implements Function {

    public static final double GRAVITATIONAL_ACCELERATION = 0.001352;

    /**
     * @param positions the positions of the objects (every 3 entries, the next object is referenced)
     * @param v2 - ignored
     * @param v3 - ignored
     * @return a vector of the gravitational force working on the rocket
     */
    @Override
    public Vector f(Vector positions, Vector v2, Vector v3, double h, double t) {
        double[] result = new double[]{0, GRAVITATIONAL_ACCELERATION, 0};
        return new Vector(result);
    }
}
