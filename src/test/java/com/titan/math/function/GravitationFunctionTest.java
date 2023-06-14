package com.titan.math.function;


import com.titan.math.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GravitationFunctionTest {

    /**
     * Simple system with only 2 objects: Sun and Planet <br>
     * Sun is at the center and weights 1e28; Planet is 100 km away in the x direction and weights 1; <br>
     * When the following formula is applied,
     * <p>
     *  F = - sum( G * mi * mj * (pi - pj) / (||pi - pj||^3)
     * <p>
     * the result of the equation for x is:
     * <p>
     *  -1 * (6.6743e-20 * 1 * 1e28 * (100 - 0) / (||100 - 0||^3) <br>
     *  = -1 * (6.6743e8 * (100) / (1e6) <br>
     *  = -1 * (6.6743e8 * (1e-4) <br>
     *  = -1 * (66743) = -66743 <p>
     *
     *  For y and z the result is 0, as "0.0 - 0.0 = 0", hence everything is multiplied with 0 at some point in the formula.
     *  Since the doubles can be positive or negative even if the value is 0,
     *  the result is -0.0 because everything is multiplied by -1 at the end.
     */
    @Test
    public void testFunctionWithSimpleValuesForXForOnePlanet() {
        // given
        Vector positions = new Vector(new double[]{
                0.0, 0.0, 0.0, // Sun
                100, 0.0, 0.0 // Planet
        });
        Vector velocities = new Vector(new double[]{ // is not used in the function
                0.0, 0.0, 0.0, // Sun
                0.0, 0.0, 0.0 // Planet
        });
        Vector masses = new Vector(new double[]{
                1e28, // Sun
                1 // Planet
        });
        GravitationFunction f = new GravitationFunction();

        // when
        Vector result = f.f(positions, velocities, masses, 1, 1);

        // then
        assertEquals(-66743, result.getValue(3), 0); // x value of the planet
        assertEquals(-0.0, result.getValue(4), 0); // y value of the planet
        assertEquals(-0.0, result.getValue(5), 0); // z value of the planet
    }

    /**
     * same calculations as for the x value
     */
    @Test
    public void testFunctionWithSimpleValuesForYForOnePlanet() {
        // given
        Vector positions = new Vector(new double[]{
                0.0, 0.0, 0.0, // Sun
                0.0, 100, 0.0 // Planet
        });
        Vector velocities = new Vector(new double[]{ // is not used in the function
                0.0, 0.0, 0.0, // Sun
                0.0, 0.0, 0.0 // Planet
        });
        Vector masses = new Vector(new double[]{
                1e28, // Sun
                1 // Planet
        });
        GravitationFunction f = new GravitationFunction();

        // when
        Vector result = f.f(positions, velocities, masses, 1, 1);

        // then
        assertEquals(-0.0, result.getValue(3), 0); // x value of the planet
        assertEquals(-66743, result.getValue(4), 0); // y value of the planet
        assertEquals(-0.0, result.getValue(5), 0); // z value of the planet
    }

    /**
     * same calculations as for the x value
     */
    @Test
    public void testFunctionWithSimpleValuesForZForOnePlanet() {
        // given
        Vector positions = new Vector(new double[]{
                0.0, 0.0, 0.0, // Sun
                0.0, 0.0, 100 // Planet
        });
        Vector velocities = new Vector(new double[]{ // is not used in the function
                0.0, 0.0, 0.0, // Sun
                0.0, 0.0, 0.0 // Planet
        });
        Vector masses = new Vector(new double[]{
                1e28, // Sun
                1 // Planet
        });
        GravitationFunction f = new GravitationFunction();

        // when
        Vector result = f.f(positions, velocities, masses, 1, 1);

        // then
        assertEquals(-0.0, result.getValue(3), 0); // x value of the planet
        assertEquals(-0.0, result.getValue(4), 0); // y value of the planet
        assertEquals(-66743, result.getValue(5), 0); // z value of the planet
    }
}