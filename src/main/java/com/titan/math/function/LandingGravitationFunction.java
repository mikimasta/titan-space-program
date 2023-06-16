package com.titan.math.function;

import com.titan.math.Vector;

public class LandingGravitationFunction implements Function {

    public static final double GRAVITATIONAL_ACCELERATION = 0.001352;
    private final double DRAG_COEFFICIENT = 0.47; // sphere -> see (https://www.engineersedge.com/calculators/air_resistance_force_14729.htm)
    private final double AREA = Math.pow(4, 2) * Math.PI; // area of the sphere facing the air (area of a circle)

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

        result = Vector.add(result, airResistance(position, velocity));
        System.out.println("air resist: " + airResistance(position, velocity));
        // right now -0.34455247229176383
        //-0.001352
        System.out.println("pos: " + position);
        System.out.println("vel: " + velocity);
        System.out.println("res: " + result);
        System.out.println("----------------------------------------");

        if(result.getValue(0) > 0) {
            System.out.println();
        }

        return result;
    }

    // NASA: https://www.grc.nasa.gov/www/k-12/VirtualAero/BottleRocket/airplane/falling.html
    private Vector airResistance(Vector position, Vector velocity) {

        Vector velocitySquare = new Vector(new double[]{
                Math.pow(velocity.getValue(0), 2),
                Math.pow(velocity.getValue(1), 2),
                0});

        if (velocity.getValue(0) < 0) {
            velocitySquare = new Vector(new double[]{
                    - velocitySquare.getValue(0),
                    velocitySquare.getValue(1),
                    velocitySquare.getValue(2)});
        }

        if (velocity.getValue(1) < 0) {
            velocitySquare = new Vector(new double[]{
                    velocitySquare.getValue(0),
                    - velocitySquare.getValue(1),
                    velocitySquare.getValue(2)});
        }

        return velocitySquare
                .multiplyByScalar(0.5)
                .multiplyByScalar(calculateAirDensity(position.getValue(1))) // density in kg/m^3
                .multiplyByScalar(AREA)
                .multiplyByScalar(DRAG_COEFFICIENT)
                .multiplyByScalar(-1);
    }

    private double calculateAirDensity(double altitude) {
 //       double p = calculatePressure(altitude); // pressure in pascal or bar whatever
        double p = calculatePressure(altitude) * 100000; // pressure in pascal (bar * 100,000)
        double m = 0.02896; // molar mass of air

        double r = 8.314; // ideal gas constant (8.314 J/(mol·K))
        double t = calculateTemperature(altitude); // temperature in kelvin

        // density in kg/m^3
        double density = (p * m) / (r * t) * 1.5; // Air density + 50% (according to NASA)

        return density;
    }

    /**
     * Matlab polyfit for the coefficients - data from NASA (https://attic.gsfc.nasa.gov/huygensgcms/Titan_atmos.gif) <br>
     * Matlab function: f6 = @(x) -0.000000000282784.*x.^6 + 0.000000160592161.*x.^5  -0.000033550883549.*x.^4 + 0.003020265171911.*x.^3 -0.095206120316128.*x.^2 + 0.219198689819765.*x + 88.631154566255688
     * @param altitude
     * @return
     */
    private double calculateTemperature(double altitude) {
        return -0.000000000282784 * Math.pow(altitude, 6)
                + 0.000000160592161 * Math.pow(altitude, 5)
                -0.000033550883549 * Math.pow(altitude, 4)
                + 0.003020265171911 * Math.pow(altitude, 3)
                -0.095206120316128 * Math.pow(altitude, 2)
                + 0.219198689819765 * altitude
                + 88.631154566255688;
    }

    /**
     * Matlab polyfit for the coefficients - data from NASA (https://attic.gsfc.nasa.gov/huygensgcms/Titan_atmos.gif) <br>
     * Matlab function: f5 = @(x) -0.000000000079339.*x.^5 + 0.000000049373809.*x.^4 - 0.000011714823097.*x.^3 + 0.001320377017440.*x.^2 - 0.071004201806174.*x + 1.494389829591593
     * @param altitude
     * @return
     */
    private double calculatePressure(double altitude) {
        double result = -0.000000000079339 * Math.pow(altitude, 5)
                + 0.000000049373809 * Math.pow(altitude, 4)
                - 0.000011714823097 * Math.pow(altitude, 3)
                + 0.001320377017440 * Math.pow(altitude, 2)
                - 0.071004201806174 * altitude
                + 1.494389829591593;
        return (result < 0) ? -result : result;
    }
}
