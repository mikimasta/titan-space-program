package com.titan.math.solver;


import com.titan.math.Vector;
import com.titan.math.function.Function;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EulerSolverTest {

    /**
     *
     * Tests the euler solver for the simple function "f(x,y,z) = x+y" <br>
     * The expected values are calculated on paper like this: <p>
     * h = 1; x0 = 1; y0 = 1; z0 = 1; <p>
     *
     * x1 = x0 + h * y0 = 1 + 1 * 1 = 2 <br>
     * y1 = y0 + h * (x0 + y0) = 1 + 1 * (1 + 1) = 3 <p>
     *
     * x2 = x1 + h * y1 = 2 + 1 * 3 = 5 <br>
     * y2 = y1 + h * (x1 + y1) = 3 + 1 * (2 + 3) = 8 <p>
     *
     * x3 = x2 + h * y2 = 5 + 1 * 8 = 13 <br>
     * y3 = y2 + h * (x2 + y2) = 8 + 1 * (5 + 8) = 21 <p>
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
        EulerSolver solver = new EulerSolver();

        // when
        Vector[] w1 = solver.solve(f, x0, y0, z0, h, 1);
        Vector[] w2 = solver.solve(f, w1[0], w1[1], z0, h, 1);
        Vector[] w3 = solver.solve(f, w2[0], w2[1], z0, h, 1);

        // then
        assertEquals(2, w1[0].getValue(0), 0);
        assertEquals(3, w1[1].getValue(0), 0);

        assertEquals(5, w2[0].getValue(0), 0);
        assertEquals(8, w2[1].getValue(0), 0);

        assertEquals(13, w3[0].getValue(0), 0);
        assertEquals(21, w3[1].getValue(0), 0);
    }

    /**
     *
     * Tests the euler solver for the simple function "f(x,y,z) = 1" <br>
     * The expected values are calculated on paper like this: <p>
     * h = 0.5; x0 = 1; y0 = 1; z0 = 1; <p>
     *
     * x1 = x0 + h * y0 = 1 + 0.5 * 1 = 1.5 <br>
     * y1 = y0 + h * 1 = 1 + 0.5 * 1 = 1.5 <p>
     *
     * x2 = x1 + h * y1 = 1.5 + 0.5 * 1.5 = 2.25 <br>
     * y2 = y1 + h * 1 = 1.5 + 0.5 * 1 = 2 <p>
     *
     * x3 = x2 + h * y2 = 2.25 + 0.5 * 2 = 3.25 <br>
     * y3 = y2 + h * 1 = 2 + 0.5 * 1 = 2.5 <p>
     *
     * x4 = x3 + h * y3 = 3.25 + 0.5 * 2.5 = 4.5 <br>
     * y4 = y3 + h * 1 = 2.5 + 0.5 * 1 = 3 <p>
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
        EulerSolver solver = new EulerSolver();

        // when
        Vector[] w1 = solver.solve(f, x0, y0, z0, h, 1);
        Vector[] w2 = solver.solve(f, w1[0], w1[1], z0, h, 1);
        Vector[] w3 = solver.solve(f, w2[0], w2[1], z0, h, 1);
        Vector[] w4 = solver.solve(f, w3[0], w3[1], z0, h, 1);

        // then
        assertEquals(1.5, w1[0].getValue(0), 0);
        assertEquals(1.5, w1[1].getValue(0), 0);

        assertEquals(2.25, w2[0].getValue(0), 0);
        assertEquals(2, w2[1].getValue(0), 0);

        assertEquals(3.25, w3[0].getValue(0), 0);
        assertEquals(2.5, w3[1].getValue(0), 0);

        assertEquals(4.5, w4[0].getValue(0), 0);
        assertEquals(3, w4[1].getValue(0), 0);
    }
}