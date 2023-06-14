package com.titan.math.function;

import com.titan.math.Vector;

public class GravitationFunction implements Function {

    /**
     * gravitational constant
     */
    public static final double G = 6.6743e-20;

    /**
     * Calculates all the gravitational forces that are working in the system.
     * Iterates over all the objects based on the masses vector.
     * <p>
     * Gravitational force of all the objects j pulling on object i are calculated with this formula:
     * <p>
     *  F = - sum( G * mi * mj * (pi - pj) / (||pi - pj||^3)
     * <p>
     * where: <br>
     * - mi and mj are the masses of the objects i and j    <br>
     * - pi and pj are the positions of the objects i and j <br>
     *
     * @param positions the positions of the objects (every 3 entries, the next object is referenced)
     * @param ignore this function does not use this vector in its calculations
     * @param masses the masses of the objects (same order as in the positions)
     * @return a vector of the gravitational forces working in the system
     */
    @Override
    public Vector f(Vector positions, Vector ignore, Vector masses, double h, double t) {
        double[] result = new double[positions.getSize()];

        for (int i = 1; i < masses.getSize(); i++) { // i starts at 1 to skip the sun
            Vector force = new Vector(new double[]{0.0, 0.0, 0.0});
            for (int j = 0; j < masses.getSize(); j++) {
                if (j != i) {
                    // position of object i
                    Vector p1 = new Vector(new double[]{
                            positions.getValues()[i*3],
                            positions.getValues()[i*3 + 1],
                            positions.getValues()[i*3 + 2]
                    });
                    // position of object j
                    Vector p2 = new Vector(new double[]{
                            positions.getValues()[j*3],
                            positions.getValues()[j*3 + 1],
                            positions.getValues()[j*3 + 2]
                    });
                    Vector positionsDelta = p1.subtract(p2);

                    // gravitational force of object j pulling on object i
                    force = force.add(positionsDelta
                            .multiplyByScalar(
                                    (G * masses.getValues()[i] * masses.getValues()[j])
                                            / Math.pow(positionsDelta.getLength(), 3))
                    );
                }
            }
            force = force.multiplyByScalar(-1/masses.getValues()[i]);
            result[i*3] = force.getValues()[0];
            result[i*3 + 1] = force.getValues()[1];
            result[i*3 + 2] = force.getValues()[2];
        }
        return new Vector(result);
    }
}
