package com.titan.math.function;

import com.titan.math.Vector;

public class LandingGravitationFunction implements Function {

    public static final double GRAVITATIONAL_ACCELERATION = 0.001352;

    /**
     * @param position (position of the module {x, y, angle})
     * @param velocity - ignored (velocity {x, y, rotation})
     * @param engineThrust - ignored (acceleration {u (main thrust), v (rotation thrust)})
     * @return a vector of the gravitational force working on the rocket
     */
    @Override
    public Vector f(Vector position, Vector velocity, Vector engineThrust, double h, double t) {
        double u = Math.min(0.01352, Math.abs(engineThrust.getValue(0)));
        double v;
        if (engineThrust.getValue(1) >= 0) {
            v = Math.min(1, engineThrust.getValue(1));
        } else {
            v = Math.max(-1, engineThrust.getValue(1));
        }
        //System.out.println("U: " + u);

        Vector result = new Vector(new double[]{
                u * Math.sin(Math.toRadians(position.getValue(2))),
                u * Math.cos(Math.toRadians(position.getValue(2))) - GRAVITATIONAL_ACCELERATION,
                v});

        return result;
    }
}
