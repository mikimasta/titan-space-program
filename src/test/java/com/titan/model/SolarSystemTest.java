package com.titan.model;

import com.titan.math.Vector;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class SolarSystemTest {

    @Test
    public void testGetCelestialObjects() {
        // given
        SolarSystem s = new SolarSystem();

        // when
        ArrayList<CelestialObject> objects = s.getCelestialObjects();

        // then
        assertEquals("Sun", objects.get(0).getName());
        assertEquals("Mercury", objects.get(1).getName());
        assertEquals("Venus", objects.get(2).getName());
        assertEquals("Earth", objects.get(3).getName());
        assertEquals("Moon", objects.get(4).getName());
        assertEquals("Mars", objects.get(5).getName());
        assertEquals("Jupiter", objects.get(6).getName());
        assertEquals("Saturn", objects.get(7).getName());
        assertEquals("Titan", objects.get(8).getName());
        assertEquals("Neptune", objects.get(9).getName());
        assertEquals("Uranus", objects.get(10).getName());
    }

    @Test
    public void testCreateRocket() {
        // given
        SolarSystem s = new SolarSystem();
        String rocketName = "Rocket 1";
        double rocketMass = 1;
        Vector earthVelocity = new Vector(new double[]{5.05e00, -2.94e01, 1.71e-03});

        // when
        CelestialObject result = s.createRocketOnEarth(rocketName, rocketMass);

        // then
        assertEquals(rocketName, result.getName());
        assertEquals(rocketMass, result.getM(), 0);
        assertEquals(earthVelocity, result.getVelocity());
    }

    @Test
    public void testStageRocket() {
        // given
        SolarSystem s = new SolarSystem();
        Rocket rocket = s.createRocketOnEarth("Rocket 505", 50000);
        assertEquals(11, s.getCelestialObjects().size());

        // when
        s.stageRocket(rocket);

        // then
        assertEquals(12, s.getCelestialObjects().size());
        assertEquals(rocket, s.getCelestialObjects().get(11));
    }

    @Test
    public void testGetAllPositions() {
        // given
        SolarSystem s = new SolarSystem();

        // when
        Vector result = s.getAllPositions();

        // then
        assertEquals(33, result.getSize(), 0);
        // Sun-position
        assertEquals(0, result.getValue(0), 0);
        assertEquals(0, result.getValue(1), 0);
        assertEquals(0, result.getValue(2), 0);
        // Earth-position
        assertEquals(-1.48e08, result.getValue(9), 0);
        assertEquals(-2.78e07, result.getValue(10), 0);
        assertEquals(3.37e04, result.getValue(11), 0);
    }

    @Test
    public void testGetAllVelocities() {
        // given
        SolarSystem s = new SolarSystem();

        // when
        Vector result = s.getAllVelocities();

        // then
        assertEquals(33, result.getSize(), 0);
        // Sun-velocity
        assertEquals(0, result.getValue(0), 0);
        assertEquals(0, result.getValue(1), 0);
        assertEquals(0, result.getValue(2), 0);
        // Earth-velocity
        assertEquals(5.05e00, result.getValue(9), 0);
        assertEquals(-2.94e01, result.getValue(10), 0);
        assertEquals(1.71e-03, result.getValue(11), 0);
    }

    @Test
    public void testGetAllMasses() {
        // given
        SolarSystem s = new SolarSystem();

        // when
        Vector result = s.getAllMasses();

        // then
        assertEquals(11, result.getSize(), 0);
        // Sun-mass
        assertEquals(1.99e30, result.getValue(0), 0);
        // Earth-mass
        assertEquals(5.97e24, result.getValue(3), 0);
    }

    @Test
    public void testSetAllPositions() {
        // given
        SolarSystem s = new SolarSystem();
        Vector givenPositions = new Vector(new double[] {
                 1, 2, 3,  4, 5, 6,  7, 8, 9,
                10,11,12, 13,14,15, 16,17,18,
                19,20,21, 22,23,24, 25,26,27,
                28,29,30, 31,32,33
        });

        // when
        s.setAllPositions(givenPositions);

        // then
        assertEquals(33, s.getAllPositions().getSize(), 0);
        assertEquals(5, s.getAllPositions().getValue(4), 0);
        assertEquals(9, s.getAllPositions().getValue(8), 0);
        assertEquals(17, s.getAllPositions().getValue(16), 0);
        assertEquals(33, s.getAllPositions().getValue(32), 0);
    }

    @Test
    public void testSetAllVelocities() {
        // given
        SolarSystem s = new SolarSystem();
        Vector givenVelocities = new Vector(new double[] {
                1, 2, 3,  4, 5, 6,  7, 8, 9,
                10,11,12, 13,14,15, 16,17,18,
                19,20,21, 22,23,24, 25,26,27,
                28,29,30, 31,32,33
        });

        // when
        s.setAllVelocities(givenVelocities);

        // then
        assertEquals(33, s.getAllVelocities().getSize(), 0);
        assertEquals(5, s.getAllVelocities().getValue(4), 0);
        assertEquals(9, s.getAllVelocities().getValue(8), 0);
        assertEquals(17, s.getAllVelocities().getValue(16), 0);
        assertEquals(33, s.getAllVelocities().getValue(32), 0);
    }
}