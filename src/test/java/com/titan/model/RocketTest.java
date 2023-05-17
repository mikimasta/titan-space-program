package com.titan.model;

import com.titan.math.Vector;
import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RocketTest {

    /**
     *
     * Tests the rocket's velocity after the fireEngine() method with simple parameters: <p>
     *
     * initialVelocity = [1, 1, 1] <br>
     * force (of the engine) = [1, 1, 1] <br>
     * mass of the rocket = 20 <br>
     * stepSize = 10 <p>
     *
     * The resulting velocity after the engine fires is:
     * v = initialVelocity + impulse/mass <br>
     * where impulse = force * stepSize = [1, 1, 1] * 10 = [10, 10, 10] hence <br>
     * v = [1, 1, 1] + [10, 10, 10] / 20 <br>
     * = [1, 1, 1] + [0.5, 0.5, 0.5] <br>
     * = [1.5, 1.5, 1.5]
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
        r.fireEngine(force, stepSize);

        // then
        assertEquals(new Vector(new double[]{1.5, 1.5, 1.5}), r.getVelocity());
    }

    /**
     *
     * Tests the rocket's fuel consumption after the fireEngine() method with simple parameters: <p>
     *
     * force (of the engine) = [3, 4, 0] <br>
     * mass of the rocket = 20 <br>
     * stepSize = 1 <p>
     *
     * The resulting fuel consumption after the engine fires is:
     * C = ||impulse|| * mass * stepSize^(-1) <br>
     * where impulse = force * stepSize = [3, 4, 0] * 1 = [3, 4, 0] hence <br>
     * C = ||[3, 4, 0]|| * 20 * 1 = 5 * 20 = 100
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
        Vector force = new Vector(new double[]{3, 4, 0});
        int stepSize = 1;

        // when
        r.fireEngine(force, stepSize);
        double fuel = r.getFuelConsumption().get(0);

        // then
        assertEquals(100, fuel, 0);
    }
}