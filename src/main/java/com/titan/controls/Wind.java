package com.titan.controls;

import com.titan.math.Vector;
import com.titan.model.LandingModule;

import java.util.Random;

public class Wind {

    public enum WindType {
        NO_WIND(1),
        LIGHT_WIND(12),
        MODERATE_WIND(8),
        STRONG_WIND(6),
        STORM(4),
        HURRICANE(1);
        final double strengthFactor;

        WindType(double strengthFactor) {
            this.strengthFactor = strengthFactor;
        }
    }

    private static final double DRAG_COEFFICIENT = 0.47; // sphere -> see (https://www.engineersedge.com/calculators/air_resistance_force_14729.htm)
    private final double AREA = Math.pow(4, 2) * Math.PI; // area of the sphere facing the air (area of a circle)
    private double currentTime;
    private final WindType windType;
    private Vector currentWindSpeed = new Vector(new double[]{0,0,0});
    private final double c1;
    private final double c2;
    private final double shift;
    private int windDirection = 1;

    public Wind(WindType windType) {
        this.windType = windType;
        Random r = new Random();
        c1 = r.nextDouble(0.3, 0.5);
        c2 = r.nextDouble(0.2, 0.3);
        shift = 1;
        windDirection = (r.nextInt(0,2) == 0) ? -1 : 1;
    }

    Vector calculateVelocity(LandingModule module) {
        if (module.getY() > 0.05 && windType != WindType.NO_WIND) {

            double windSpeed = windFunction(c1, c2, getCurrentTime(), shift) * 100 / windType.strengthFactor;
            double windAngle = windFunction(c1, c2, getCurrentTime(), 0) * 100 / 5;

            currentWindSpeed = new Vector(new double[]{
                windDirection * windSpeed / 3600d, 
                windAngle / 3600d,
                0});
            return currentWindSpeed;
        }
        return new Vector(new double[]{0, 0, 0});
    }

    public double getWindSpeed() {
        return currentWindSpeed.getLength() * 3600;
    }

    public double getWindAngle() {
        double angle  = Math.asin(currentWindSpeed.getValue(1)/currentWindSpeed.getLength());

        //currentWindSpeed.getValue(0) < 0 ? -degrees : degrees;
        return currentWindSpeed.getValue(0) < 0 ? 180 - Math.toDegrees(angle) : Math.toDegrees(angle);
    }

    double getCurrentTime() {
        return currentTime;
    }

    double windFunction(double c1, double c2, double step, double shift) {
        return c1 * Math.cos(4.667 * step/3600) + c2 * Math.cos(12.22 * step/3600) + shift;
    } 

    public void blow(LandingModule module, double stepSize, double currentTime) {
        this.currentTime = currentTime;
        if (module.getY() < 0.02) return;
        Vector windVelocity = calculateVelocity(module).subtract(module.getVelocity()).multiplyByScalar(1000); // m/s
        Vector force = airResistance(module.getPosition(), windVelocity);
        applyForce(module, force, stepSize);
    }

    private void applyForce(LandingModule module, Vector force, double stepSize) {
        Vector impulse = force.multiplyByScalar(stepSize); // kg * m/s
        impulse = impulse.multiplyByScalar(1.0/1000d); // kg * km/s
        Vector velocity = impulse.multiplyByScalar(1.0/module.getM()); // km/s
        module.updateVelocity(module.getVelocity().add(velocity));
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
                .multiplyByScalar(DRAG_COEFFICIENT);
    }

    public static double calculateAirDensity(double altitude) {
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
    public static double calculateTemperature(double altitude) {
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
    public static double calculatePressure(double altitude) {
        double result = -0.000000000079339 * Math.pow(altitude, 5)
                + 0.000000049373809 * Math.pow(altitude, 4)
                - 0.000011714823097 * Math.pow(altitude, 3)
                + 0.001320377017440 * Math.pow(altitude, 2)
                - 0.071004201806174 * altitude
                + 1.494389829591593;
        return (result < 0) ? -result : result;
    }

}
