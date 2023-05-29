package com.titan.model;

import com.titan.math.Vector;
import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RocketTest {

    /**
     *
     * Tests the rocket's velocity after the fireEngineWithForce() method with simple parameters: <p>
     *
     * initialVelocity = [1, 1, 1] <br>
     * force (of the engine) = [1, 1, 1] <br>
     * mass of the rocket = 20 <br>
     * stepSize = 10 <p>
     *
     * The resulting velocity after the engine fires is:
     * v = initialVelocity + impulse/mass <br>
     * where impulse = force * stepSize / 1000 = [1, 1, 1] * 10 / 1000 = [0.001, 0.001, 0.001] hence <br>
     * v = [1, 1, 1] + [0.001, 0.001, 0.001] / 20 <br>
     * = [1, 1, 1] + [0.0005, 0.0005, 0.0005] <br>
     * = [1.0005, 1.0005, 1.0005]
     */
    @Test
    public void testFireEngineVelocity() {
        // given
        double mass = 20;
        Vector velocity = new Vector(new double[]{1, 1, 1});
        Rocket r = new Rocket(
                "Rocket 505",
                mass,
                new Vector(new double[]{1, 1, 1}),
                velocity,
                1,
                Color.AQUA,
                1);
        Vector force = new Vector(new double[]{1, 1, 1});
        int stepSize = 10;

        // when
        r.fireEngineWithForce(force, stepSize);

        // then
        assertEquals(new Vector(new double[]{1.0005, 1.0005, 1.0005}), r.getVelocity());
    }

    /**
     *
     * Tests the rocket's fuel consumption after the fireEngineWithForce() method with simple parameters: <p>
     *
     * force (of the engine) = [30, 40, 0] <br>
     * mass of the rocket = 20 <br>
     * stepSize = 1 <p>
     *
     * The resulting fuel consumption after the engine fires is:
     * C = ||impulse|| * mass * stepSize^(-1) <br>
     * where impulse = force * stepSize / 1000 = [30, 40, 0] * 1 / 1000 = [0.03, 0.04, 0] hence <br>
     * C = ||[0.03, 0.04, 0]|| * 20 * 1 = 0.05 * 20 = 1
     */
    @Test
    public void testFireEngineFuelConsumption() {
        // given
        double mass = 20;
        Rocket r = new Rocket(
                "Rocket 505",
                mass,
                new Vector(new double[]{1, 1, 1}),
                new Vector(new double[]{1, 1, 1}),
                1,
                Color.AQUA,
                1);
        Vector force = new Vector(new double[]{30, 40, 0});
        int stepSize = 1;

        // when
        r.fireEngineWithForce(force, stepSize);
        double fuel = r.getFuelConsumption().get(0);

        // then
        assertEquals(1, fuel, 0);
    }
}