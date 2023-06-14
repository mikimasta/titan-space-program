package com.titan.math.solver;

import com.titan.math.DifferentialEquation;
import com.titan.math.Vector;
import com.titan.math.function.Function;

/**
 * RungeKuttaSolver implements Solver: <br>
 * implementation of the Runge-Kutta-Solver approximation method <br>
 * k1 = h * f(wi, ti); <br>
 * k2 = h * f(wi + k1, ti + h/2); <br>
 * k3 = h * f(wi + k2, ti + h/2); <br>
 * k4 = h * f(wi + k3, ti); <br>
 * wi+1 = wi + 1/6 * (k1 + 2*k2 + 2*k3 + k4)
 */
public class RungeKuttaSolver implements Solver {

    /**
     * this method calculates the positions and velocities of all celestial objects after every iteration using the
     * 4 stage Runge-Kutta method
     * @param positions vector of all the positions
     * @param velocities vector of all the velocities
     * @param t step we are currently on
     */
    public Vector[] solve(Function f, Vector positions, Vector velocities, Vector masses, double h, double t) {
        Vector[] result = new Vector[2];

        Vector[] k1 = nextK(f,
                positions,
                velocities,
                masses,
                h,
                t);

        Vector[] k2 = nextK(f,
                positions.add(k1[0].multiplyByScalar(1.0 / 2)),
                velocities.add(k1[1].multiplyByScalar(1.0 / 2)),
                masses,
                h,
                t + h/2);

        Vector[] k3 = nextK(f,
                positions.add(k2[0].multiplyByScalar(1.0 / 2)),
                velocities.add(k2[1].multiplyByScalar(1.0 / 2)),
                masses,
                h,
                t + h/2);

        Vector[] k4 = nextK(f,
                positions.add(k3[0]),
                velocities.add(k3[1]),
                masses,
                h,
                t + h);

        result[0] = positions.add(
                k1[0]
                        .add(k2[0].multiplyByScalar(2))
                        .add(k3[0].multiplyByScalar(2))
                        .add(k4[0])
                .multiplyByScalar(1.0/6));

        result[1] = velocities.add(
                k1[1]
                        .add(k2[1].multiplyByScalar(2))
                        .add(k3[1].multiplyByScalar(2))
                        .add(k4[1])
                        .multiplyByScalar(1.0/6));

        return result;
    }

    /**
     * calculates the next k
     * @param f
     * @param positions
     * @param velocities
     * @param masses
     * @return
     */
    private Vector[] nextK(Function f, Vector positions, Vector velocities, Vector masses, double h, double t) {
        Vector[] k = DifferentialEquation.solve(
                f,
                positions,
                velocities,
                masses,
                h,
                t);
        k[0] = k[0].multiplyByScalar(h);
        k[1] = k[1].multiplyByScalar(h);
        return k;
    }
}
