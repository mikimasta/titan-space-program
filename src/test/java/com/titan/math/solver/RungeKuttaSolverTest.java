package com.titan.math.solver;


import com.titan.math.Vector;
import com.titan.math.function.Function;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RungeKuttaSolverTest {

    /**
     *
     * Tests the runge kutta solver for the simple function "f(x,y,z) = x+y" where derivative of f(x) = y <br>
     * The expected values are calculated on paper like this: <p>
     * h = 1; x0 = 1; y0 = 1; z0 = 1; <p>
     *
     * k1x = h * y0 = 1 * 1 = 1 <br>
     * k1y = h * (x0 + y0) = 1 * (1 + 1) = 2 <br>
     * k2x = h * (y0 + 1/2*k1y) = 1 * (1 + 1/2 * 2) = 2 <br>
     * k2y = h * ((x0 + 1/2*k1x) + (y0 + 1/2*k1y)) = 1 * ((1 + 1/2 * 1) + (1 + 1/2 * 2)) = 3.5 <br>
     * k3x = h * (y0 + 1/2*k2y) = 1 * (1 + 1/2 * 3.5) = 2.75 <br>
     * k3y = h * ((x0 + 1/2*k2x) + (y0 + 1/2*k2y)) = 1 * ((1 + 1/2 * 2) + (1 + 1/2 * 3.5)) = 4.75 <br>
     * k4x = h * (y0 + k3y) = 1 * (1 + 4.75) = 5.75 <br>
     * k4y = h * ((x0 + k3x) + (y0 + k3y)) = 1 * ((1 + 2.75) + (1 + 4.75)) = 9.5 <p>
     *
     * x1 = x0 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 1 + 1/6 * (1 + 2*2 + 2*2.75 + 5.75) = 3.7083 <br>
     * y1 = y0 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 1 + 1/6 * (2 + 2*3.5 + 2*4.75 + 9.5) = 5.6666 <p> <p>
     *
     * k1x = h * y1 = 1 * 5.6666 = 5.6666 <br>
     * k1y = h * (x1 + y1) = 1 * (3.7083 + 5.6666) = 9.3749 <br>
     * k2x = h * (y1 + 1/2*k1y) = 1 * (5.6666 + 1/2 * 9.3749) = 10.3541 <br>
     * k2y = h * ((x1 + 1/2*k1x) + (y1 + 1/2*k1y)) = 1 * ((3.7083 + 1/2 * 5.6666) + (5.6666 + 1/2 * 9.3749)) = 16.8957 <br>
     * k3x = h * (y1 + 1/2*k2y) = 1 * (5.6666 + 1/2 * 16.8957) = 14.1145<br>
     * k3y = h * ((x1 + 1/2*k2x) + (y1 + 1/2*k2y)) = 1 * ((3.7083 + 1/2 * 10.3541) + (5.6666 + 1/2 * 16.8957)) = 22.9997 <br>
     * k4x = h * (y1 + k3y) = 1 * (5.6666 + 22.9997) = 28.6663 <br>
     * k4y = h * ((x1 + k3x) + (y1 + k3y)) = 1 * ((3.7083 + 14.1145) + (5.6666 + 22.9997)) = 46.4891 <p>
     *
     * x2 = x1 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 3.7083 + 1/6 * (5.6666 + 2*10.3541 + 2*14.1145 + 28.6663) = 17.5870 <br>
     * y2 = y1 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 5.6666 + 1/6 * (9.3749 + 2*16.8957 + 2*22.9997 + 46.4891) = 28.2757 <p> <p>
     *
     * k1x = h * y2 = 1 * 28.2757 = 28.2757 <br>
     * k1y = h * (x2 + y2) = 1 * (17.5870 + 28.2757) = 45.8627 <br>
     * k2x = h * (y2 + 1/2*k1y) = 1 * (28.2757 + 1/2 * 45.8627) = 51.2071 <br>
     * k2y = h * ((x2 + 1/2*k1x) + (y2 + 1/2*k1y)) = 1 * ((17.5870 + 1/2 * 28.2757) + (28.2757 + 1/2 * 45.8627)) = 82.9319 <br>
     * k3x = h * (y2 + 1/2*k2y) = 1 * (28.2757 + 1/2 * 82.9319) = 69.7416 <br>
     * k3y = h * ((x2 + 1/2*k2x) + (y2 + 1/2*k2y)) = 1 * ((17.5870 + 1/2 * 51.2071) + (28.2757 + 1/2 * 82.9319)) = 112.9322 <br>
     * k4x = h * (y2 + k3y) = 1 * (28.2757 + 112.9322) = 141.2079 <br>
     * k4y = h * ((x2 + k3x) + (y2 + k3y)) = 1 * ((17.5870 + 69.7416) + (28.2757 + 112.9322)) = 228.5365 <p>
     *
     * x3 = x2 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 1 + 1/6 * (28.2757 + 2*51.2071 + 2*69.7416 + 141.2079) = 86.1505 <br>
     * y3 = y2 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 1 + 1/6 * (45.8627 + 2*82.9319 + 2*112.9322 + 228.5365) = 139.2969 <p> <p>
     *
     */
    @Test
    public void testSolveForLinearFunctionF() {
        // given
        Function f = (v1, v2, v3, h, t) -> v1.add(v2); // f(x,y,z) = x+y;   => z is ignored

        Vector x0 = new Vector(new double[]{1});
        Vector y0 = new Vector(new double[]{1});
        Vector z0 = new Vector(new double[]{1}); // ignored in this test
        double h = 1;
        RungeKuttaSolver solver = new RungeKuttaSolver();

        // when
        Vector[] w1 = solver.solve(f, x0, y0, z0, h, 1);
        Vector[] w2 = solver.solve(f, w1[0], w1[1], z0, h, 1);
        Vector[] w3 = solver.solve(f, w2[0], w2[1], z0, h, 1);

        // then
        assertEquals(3.7083, w1[0].getValue(0), 0.001);
        assertEquals(5.6666, w1[1].getValue(0), 0.001);

        assertEquals(17.5870, w2[0].getValue(0), 0.001);
        assertEquals(28.2757, w2[1].getValue(0), 0.001);

        assertEquals(86.1505, w3[0].getValue(0), 0.001);
        assertEquals(139.2969, w3[1].getValue(0), 0.001);
    }

    /**
     *
     * Tests the Runge Kutta solver for the simple function "f(x,y,z) = 1" where derivative of f(x) = y <br>
     * The expected values are calculated on paper like this: <p>
     * h = 0.5; x0 = 1; y0 = 1; z0 = 1; <p>
     *
     * k1x = h * y0 = 0.5 * 1 = 0.5 <br>
     * k1y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k2x = h * (y0 + 1/2*k1y) = 0.5 * (1 + 1/2 * 0.5) = 0.625 <br>
     * k2y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k3x = h * (y0 + 1/2*k2y) = 0.5 * (1 + 1/2 * 0.5) = 0.625 <br>
     * k3y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k4x = h * (y0 + k3y) = 0.5 * (1 + 0.5) = 0.75 <br>
     * k4y = h * 1 = 0.5 * 1 = 0.5 <p>
     *
     * x1 = x0 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 1 + 1/6 * (0.5 + 2*0.625 + 2*0.625 + 0.75) = 1.625 <br>
     * y1 = y0 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 1 + 1/6 * (0.5 + 2*0.5 + 2*0.5 + 0.5) = 1.5 <p> <p>
     *
     * k1x = h * y1 = 0.5 * 1.5 = 0.75 <br>
     * k1y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k2x = h * (y1 + 1/2*k1y) = 0.5 * (1.5 + 1/2 * 0.5) = 0.875 <br>
     * k2y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k3x = h * (y1 + 1/2*k2y) = 0.5 * (1.5 + 1/2 * 0.5) = 0.875 <br>
     * k3y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k4x = h * (y1 + k3y) = 0.5 * (1.5 + 0.5) = 1 <br>
     * k4y = h * 1 = 0.5 * 1 = 0.5 <p>
     *
     * x2 = x1 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 1.625 + 1/6 * (0.75 + 2*0.875 + 2*0.875 + 1) = 2.5 <br>
     * y2 = y1 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 1.5 + 1/6 * (0.5 + 2*0.5 + 2*0.5 + 0.5) = 2 <p> <p>
     *
     * k1x = h * y2 = 0.5 * 2 = 1 <br>
     * k1y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k2x = h * (y2 + 1/2*k1y) = 0.5 * (2 + 1/2 * 0.5) = 1.125 <br>
     * k2y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k3x = h * (y2 + 1/2*k2y) = 0.5 * (2 + 1/2 * 0.5) = 1.125 <br>
     * k3y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k4x = h * (y2 + k3y) = 0.5 * (2 + 0.5) = 1.25 <br>
     * k4y = h * 1 = 0.5 * 1 = 0.5 <p>
     *
     * x3 = x2 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 2.5 + 1/6 * (1 + 2*1.125 + 2*1.125 + 1.25) = 3.625 <br>
     * y3 = y2 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 2 + 1/6 * (0.5 + 2*0.5 + 2*0.5 + 0.5) = 2.5 <p> <p>
     *
     * k1x = h * y3 = 0.5 * 2.5 = 1.25 <br>
     * k1y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k2x = h * (y3 + 1/2*k1y) = 0.5 * (2.5 + 1/2 * 0.5) = 1.375 <br>
     * k2y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k3x = h * (y3 + 1/2*k2y) = 0.5 * (2.5 + 1/2 * 0.5) = 1.375 <br>
     * k3y = h * 1 = 0.5 * 1 = 0.5 <br>
     * k4x = h * (y3 + k3y) = 0.5 * (2.5 + 0.5) = 1.5 <br>
     * k4y = h * 1 = 0.5 * 1 = 0.5 <p>
     *
     * x4 = x3 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 3.625 + 1/6 * (1.25 + 2*1.375 + 2*1.375 + 1.5) = 5 <br>
     * y4 = y3 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 2.5 + 1/6 * (0.5 + 2*0.5 + 2*0.5 + 0.5) = 3 <p> <p>
     *
     */
    @Test
    public void testSolveForFunctionFEquals1WithStepSize0Point5() {
        // given
        Function f = (v1, v2, v3, h, t) -> new Vector(new double[]{1}); // f(x,y,z) = 1;

        Vector x0 = new Vector(new double[]{1});
        Vector y0 = new Vector(new double[]{1});
        Vector z0 = new Vector(new double[]{1}); // ignored in this test
        double h = 0.5;
        RungeKuttaSolver solver = new RungeKuttaSolver();

        // when
        Vector[] w1 = solver.solve(f, x0, y0, z0, h, 1);
        Vector[] w2 = solver.solve(f, w1[0], w1[1], z0, h, 1);
        Vector[] w3 = solver.solve(f, w2[0], w2[1], z0, h, 1);
        Vector[] w4 = solver.solve(f, w3[0], w3[1], z0, h, 1);

        // then
        assertEquals(1.625, w1[0].getValue(0), 0);
        assertEquals(1.5, w1[1].getValue(0), 0);

        assertEquals(2.5, w2[0].getValue(0), 0);
        assertEquals(2, w2[1].getValue(0), 0);

        assertEquals(3.625, w3[0].getValue(0), 0);
        assertEquals(2.5, w3[1].getValue(0), 0);

        assertEquals(5, w4[0].getValue(0), 0);
        assertEquals(3, w4[1].getValue(0), 0);
    }
}
