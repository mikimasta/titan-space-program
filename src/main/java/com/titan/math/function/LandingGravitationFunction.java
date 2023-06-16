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

        Vector result = new Vector(new double[]{
                engineThrust.getValue(0) * Math.sin(Math.toRadians(position.getValue(2))),
                engineThrust.getValue(0) * Math.cos(Math.toRadians(position.getValue(2))) - GRAVITATIONAL_ACCELERATION,
                engineThrust.getValue(1)});

//        Vector airResistance = airResistance(position, velocity).multiplyByScalar(1d/1000d);
//        result = Vector.add(result, airResistance);

        return result;
    }
}
