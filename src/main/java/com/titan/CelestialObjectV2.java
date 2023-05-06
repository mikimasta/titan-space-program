package com.titan;

import com.titan.gui.Titan;
import com.titan.math.Vector;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CelestialObjectV2 {

    /**
     * mass of a given celestial object
     */
    private double m;

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

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    /**
     * HashMap storing past positions of celestial objects
     */
    private HashMap<Integer, Vector> historicPositions;

    /**
     * HashMap storing past velocities of celestial objects
     */
    private HashMap<Integer, Vector> historicVelocities;

    /**
     * name of a celestial object
     */
    private String name;

    /**
     * diameter of a celestial object
     */
    private double diameter;

    /**
     * color of a celestial object
     */
    private Color color;

    /**
     * radius of a celestial object
     */
    private int radius;

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
    public CelestialObjectV2(String name, double m, Vector initialPos, Vector initialVel, double diameter, Color color, int radius) {
        historicPositions = new HashMap<>();
        historicVelocities = new HashMap<>();
        this.m = m;
        this.position = initialPos;
        this.velocity = initialVel;
        this.name = name;
        this.diameter = diameter;
        this.color = color;
        this.radius = radius;

    }


    public CelestialObjectV2(String name, double m, Vector initialPos, Vector initialVel, long diameter, Color color) {
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
        if (Titan.currentStep % (86400 / Titan.stepSize) == 0) {
            historicPositions.put(Titan.currentStep, position);
        }
    }

    /**
     * updates the current velocity of a celestial body
     * @param velocity
     */
    public void updateVelocity(Vector velocity) {
        this.velocity = velocity;
        if (Titan.currentStep % (86400 / Titan.stepSize) == 0) {
            historicVelocities.put(Titan.currentStep, velocity);
        }
    }

    /**
     * returns the HashMap containing past positions of celestial objects
     * @return
     */
    public HashMap<Integer, Vector> getHistoricPositions() {
        return historicPositions;
    }

    /**
     * sorts the elements of the hashmap and returns them as an arraylist
     * @return
     */
    public ArrayList<Vector> getHistoricPositionsVector() {
        HashMap<Integer, Vector> positions = getHistoricPositions();
        ArrayList<Integer> sortedKeys = new ArrayList<>(positions.keySet());
        Collections.sort(sortedKeys);
        ArrayList<Vector> result = new ArrayList<>();
        for (Integer i : sortedKeys) {
            result.add(positions.get(i));
        }
        return result;
    }

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
}
