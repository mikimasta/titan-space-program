package com.titan.model;

import com.titan.gui.Titan;
import com.titan.math.Vector;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Locale;

public class CelestialObject {

    public static int stepsUntilNextHistoricSave = 86400; // 86400 = 1 day

    /**
     * mass of a given celestial object
     */
    private final double m;

    /**
     * last position of a given celestial object
     */
    private Vector position;

    /**
     * last velocity of a given celestial object
     */
    private Vector velocity;

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    /**
     * ArrayList storing past positions of celestial objects
     */
    private final ArrayList<Vector> historicPositions;

    /**
     * ArrayList storing past velocities of celestial objects
     */
    private final ArrayList<Vector> historicVelocities;

    /**
     * name of a celestial object
     */
    private final String name;

    /**
     * diameter of a celestial object
     */
    private final double diameter;

    /**
     * color of a celestial object
     */
    private final Color color;

    /**
     * radius of a celestial object
     */
    private final int radius;

    /**
     * constructs a CelestialObject
     * @param name
     * @param m
     * @param initialPos
     * @param initialVel
     * @param diameter
     * @param color
     * @param radius
     */
    public CelestialObject(String name, double m, Vector initialPos, Vector initialVel, double diameter, Color color, int radius) {
        historicPositions = new ArrayList<>();
        historicVelocities = new ArrayList<>();
        this.m = m;
        updatePosition(initialPos);
        updateVelocity(initialVel);
        this.name = name;
        this.diameter = diameter;
        this.color = color;
        this.radius = radius;

    }


    public CelestialObject(String name, double m, Vector initialPos, Vector initialVel, long diameter, Color color) {
        this(name, m, initialPos, initialVel, diameter, color, 15);
    }

    /**
     *
     * @return returns the diameter of a celestial body
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * updates the current posistion of the celestial body
     * @param position
     */
    public void updatePosition(Vector position) {
        this.position = position;
         if (Titan.running && Titan.currentStep % (stepsUntilNextHistoricSave / Titan.stepSize) == 0) {
             historicPositions.add(position);
         }
    }

    /**
     * updates the current velocity of a celestial body
     * @param velocity
     */
    public void updateVelocity(Vector velocity) {
        this.velocity = velocity;
        if (Titan.running && Titan.currentStep % (stepsUntilNextHistoricSave / Titan.stepSize) == 0) {
            historicVelocities.add(velocity);
        }
    }

    /**
     * returns the HashMap containing past positions of celestial objects
     * @return
     */
    public ArrayList<Vector> getHistoricPositions() {
        return historicPositions;
    }

    /**
     * sorts the elements of the hashmap and returns them as an arraylist
     * @return
     */
    // public ArrayList<Vector> getHistoricPositionsVector() {
    //     HashMap<Vector> positions = getHistoricPositions();
    //     ArrayList<Integer> sortedKeys = new ArrayList<>(positions.keySet());
    //     Collections.sort(sortedKeys);
    //     ArrayList<Vector> result = new ArrayList<>();
    //     for (Integer i : sortedKeys) {
    //         result.add(positions.get(i));
    //     }
    //     return result;
    // }

    /**
     * returns the mass of a celestial object
     * @return
     */
    public double getM() {
        return m;
    }

    /**
     * returns the name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * returns a color of a celestial object
     * @return
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Celestial Object: " + name;
    }

    /**
     * returns the radius of a celestial body
     * @return
     */
    public int getRadius() {
        return radius;
    }

    public String toCSVString() {
        return String.format(Locale.ENGLISH, "%s,%e,%e,%e,%e,%e,%e,%e,%d,%s,%d",
                name,
                position.getValue(0),
                position.getValue(1),
                position.getValue(2),
                velocity.getValue(0),
                velocity.getValue(1),
                velocity.getValue(2),
                m,
                (int) diameter,
                color.toString(),
                radius
                );
    }
}
