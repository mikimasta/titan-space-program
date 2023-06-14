package com.titan.math.solver;

import com.titan.math.Vector;
import com.titan.math.function.Function;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdamsBashforth2ndOrderTest {

    /**
     *
     * Tests the Adams-Bashforth 2nd Order solver for the simple function "f(x, y, z) = x + y" <br>
     * The expected values are calculated on paper like this: <p>
     * h = 1; x0 = 1; y0 = 1; z0 = 1; <p>
     *
     * x1 = x0 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 1 + 1/6 * (1 + 2*2 + 2*2.75 + 5.75) = 3.708 (via RK4) <br>
     * y1 = y0 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 1 + 1/6 * (2 + 2*3.5 + 2*4.75 + 9.5) = 5.666 (via RK4) <p> <p>
     *
     * x2 = x1 + 1/2 * ((3 * y1) - y0) = 3.7083 + 1/2 * ((3 * 5.666) - 1) = 11.703 <br>
     * y2 = y1 + 1/2 * (3 * (y1 + f(x1)) - (y0 + f(x0))) = 5.666 + 1/2 * ((3 * 9.374)  - 2) = 18.727 <p>
     *
     * x3 = x2 + 1/2 * ((3 * y2) - y1) = 11.707 + 1/2 * ((3 * 18.7285) - 5.666) = 36.9645 <br>
     * y3 = y2 + 1/2 * ((3 * y2 + f(x2)) - y1 + f(x1)) = 18.727 + 1/2 * ((3 * 30.432) - 9.374) = 59.692 <p>
     *
     */
    @Test
    public void testSolveForLinearFunctionF() {
        // given
        Function f = (v1, v2, v3, h, t) -> v1.add(v2); // f(x,y,z) = x + y; z is ignored

        Vector positions = new Vector(new double[]{1});
        Vector velocities = new Vector(new double[]{1});
        Vector masses = new Vector(new double[]{1});
        double h = 1;
        AdamsBashforth2ndOrderSolver solver = new AdamsBashforth2ndOrderSolver();


        // when
        Vector[] w1 = solver.solve(f, positions, velocities, masses, h, 1);
        Vector[] w2 = solver.solve(f, w1[0], w1[1], masses, h, 1);
        Vector[] w3 = solver.solve(f, w2[0], w2[1], masses, h, 1);

        // then
        assertEquals(3.708, w1[0].getValue(0), 0.01);
        assertEquals(5.666, w1[1].getValue(0), 0.01);

        assertEquals(11.703, w2[0].getValue(0), 0.01);
        assertEquals(18.727, w2[1].getValue(0), 0.01);

        assertEquals(36.965, w3[0].getValue(0), 0.01);
        assertEquals(59.692, w3[1].getValue(0), 0.01);
    }

    /**
     *
     * Tests the Adams-Bashforth 2nd Order solver for the function "f(x, y, z) = 1" with a step size of 0.5. <br>
     * The expected values are calculated on paper like this: <p>
     * h = 0.5; x0 = 1; y0 = 1; z0 = 1; <p>
     *
     * x1 = x0 + 1/6 * (k1x + 2*k2x + 2*k3x + k4x) = 1 + 1/6 * (0.5 + 2*0.625 + 2*0.625 + 0.75) = 1.625 (via RK4)<br>
     * y1 = y0 + 1/6 * (k1y + 2*k2y + 2*k3y + k4y) = 1 + 1/6 * (0.5 + 2*0.5 + 2*0.5 + 0.5) = 1.5 (via RK4)
     * x2 = x1 + 0.5/2 * ((3 * y1) - y0) = 1.625 + 0.5/2 * ((3 * 1.5) - 1) = 2.5 <br>
     * y2 = y1 + 0.5/2 * ((3 * f(y1) - f(y0)) = 1.5 + 0.5/2 * ((3 * 1) - 1) = 2  <p>
     *
     * x3 = x2 + 0.5/2 * ((3 * y2) - y1) = 2.5 + 0.5/2 * ((3 * 2) - 1.5) = 3.625 <br>
     * y3 = y2 + 0.5/2 * ((3 * f(y2) - f(y1)) = 2 + 0.5/2 * ((3 * 1) - 1) = 2.5 <p>
     *
     * x4 = x3 + 0.5/2 * ((3 * y3) - y2) = 3.625 + 0.5/2 * ((3 * 2.5) - 2) = 5 <br>
     * y4 = y3 + 0.5/2 * ((3 * f(y3) - f(y2)) = 2.5 + 0.5/2 * ((3 * 1) - 1) = 3 <p>
     *
     */
    @Test
    public void testSolveForFunctionFEquals1WithStepSize0Point5() {
        // given
        Function f = (v1, v2, v3, h, t) -> new Vector(new double[]{1}); // f(x,y,z) = 1;

        Vector positions = new Vector(new double[]{1});
        Vector velocities = new Vector(new double[]{1});
        Vector masses = new Vector(new double[]{1});
        double h = 0.5;
        AdamsBashforth2ndOrderSolver solver = new AdamsBashforth2ndOrderSolver();

        // when
        Vector[] w1 = solver.solve(f, positions, velocities, masses, h, 1);
        Vector[] w2 = solver.solve(f, w1[0], w1[1], masses, h, 1);
        Vector[] w3 = solver.solve(f, w2[0], w2[1], masses, h, 1);
        Vector[] w4 = solver.solve(f, w3[0], w3[1], masses, h, 1);

        // then
        assertEquals(1.625, w1[0].getValue(0), 0.01);
        assertEquals(1.5, w1[1].getValue(0), 0.01);

        assertEquals(2.5, w2[0].getValue(0), 0.01);
        assertEquals(2, w2[1].getValue(0), 0.01);

        assertEquals(3.625, w3[0].getValue(0), 0.01);
        assertEquals(2.5, w3[1].getValue(0), 0.01);

        assertEquals(5, w4[0].getValue(0), 0.01);
        assertEquals(3, w4[1].getValue(0), 0.01);
    }


}