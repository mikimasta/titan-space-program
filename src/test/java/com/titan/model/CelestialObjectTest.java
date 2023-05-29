package com.titan.model;

import com.titan.gui.Titan;
import com.titan.math.Vector;
import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CelestialObjectTest {

    @Test
    public void testGetPosition() {
        // given
        Vector givenPosition = new Vector(new double[]{1, 2, 3});
        CelestialObject o = getCelestialObjectWithGivenPositionAndVelocity(givenPosition, null);

        // when
        Vector result = o.getPosition();

        // then
        assertEquals(givenPosition, result);
    }

    @Test
    public void testGetVelocity() {
        // given
        Vector givenVelocity = new Vector(new double[]{1, 2, 3});
        CelestialObject o = getCelestialObjectWithGivenPositionAndVelocity(null, givenVelocity);

        // when
        Vector result = o.getVelocity();

        // then
        assertEquals(givenVelocity, result);
    }

    @Test
    public void testGetDiameter() {
        // given
        double givenDiameter = 1.234;
        CelestialObject o = getCelestialObjectWithGivenMassAndDiameter(0, givenDiameter);

        // when
        double result = o.getDiameter();

        // then
        assertEquals(givenDiameter, result,0);
    }

    @Test
    public void testUpdatePosition() {
        // given
        Vector givenPosition = new Vector(new double[]{99, 88, 77});
        CelestialObject o = getCelestialObjectWithDefaultValues();

        // when
        o.updatePosition(givenPosition);

        // then
        assertEquals(givenPosition, o.getPosition());
    }

    @Test
    public void testUpdateVelocity() {
        // given
        Vector givenVelocity = new Vector(new double[]{99, 88, 77});
        CelestialObject o = getCelestialObjectWithDefaultValues();

        // when
        o.updateVelocity(givenVelocity);

        // then
        assertEquals(givenVelocity, o.getVelocity());
    }

    @Test
    public void testGetHistoricPositions() {
        // given
        Vector givenPosition1 = new Vector(new double[]{99, 88, 77});
        Vector givenPosition2 = new Vector(new double[]{88, 77, 66});
        Vector givenPosition3 = new Vector(new double[]{77, 66, 55});
        Vector givenPosition4 = new Vector(new double[]{66, 55, 44});
        Vector[] givenPositions = {givenPosition1, givenPosition2, givenPosition3, givenPosition4};
        Titan.currentStep = 0;
        Titan.stepSize = 1;
        Titan.running = true;

        // when
        CelestialObject o = getCelestialObjectWithGivenPositionAndVelocity(givenPosition1, null);
        o.updatePosition(givenPosition2);
        o.updatePosition(givenPosition3);
        o.updatePosition(givenPosition4);

        // then
        assertArrayEquals(givenPositions, o.getHistoricPositions().toArray());
    }

    @Test
    public void testGetMass() {
        // given
        double givenMass = 1.234;
        CelestialObject o = getCelestialObjectWithGivenMassAndDiameter(givenMass, 0);

        // when
        double result = o.getM();

        // then
        assertEquals(givenMass, result, 0);
    }

    @Test
    public void testGetName() {
        // given
        String givenName = "This is a name";
        CelestialObject o = getCelestialObjectWithGivenNameAndColor(givenName, null);

        // when
        String result = o.getName();

        // then
        assertEquals(givenName, result);
    }

    @Test
    public void testGetColor() {
        // given
        Color givenColor = Color.PURPLE;
        CelestialObject o = getCelestialObjectWithGivenNameAndColor(null, givenColor);

        // when
        Color result = o.getColor();

        // then
        assertEquals(givenColor, result);
    }

    @Test
    public void testToString() {
        // given
        CelestialObject o = getCelestialObjectWithGivenNameAndColor("Earth 2.0", null);
        String expected = "Celestial Object: Earth 2.0";

        // when
        String result = o.toString();

        // then
        assertEquals(expected, result);
    }

    private static CelestialObject getCelestialObjectWithDefaultValues() {
        return new CelestialObject("Object 1", 1.0, new Vector(new double[]{0}), new Vector(new double[]{0}), 1.0, Color.RED, 1);
    }

    private static CelestialObject getCelestialObjectWithGivenPositionAndVelocity(Vector position, Vector velocity) {
        return new CelestialObject("Object 1", 1.0, position, velocity, 1.0, Color.RED, 1);
    }

    private static CelestialObject getCelestialObjectWithGivenMassAndDiameter(double mass, double diameter) {
        return new CelestialObject("Object 1", mass, new Vector(new double[]{0}), new Vector(new double[]{0}), diameter, Color.RED, 1);
    }

    private static CelestialObject getCelestialObjectWithGivenNameAndColor(String name, Color color) {
        return new CelestialObject(name, 1.0, new Vector(new double[]{0}), new Vector(new double[]{0}), 1.0, color, 1);
    }

}