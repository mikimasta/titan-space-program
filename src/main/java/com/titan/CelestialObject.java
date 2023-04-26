package com.titan;

import com.titan.gui.Titan;
import com.titan.math.Vector3d;
import javafx.scene.paint.Color;

import java.util.*;

public class CelestialObject {

    /**
     * mass of a given celestial object
     */
    private double m;

    /**
     * last position of a given celestial object
     */
    private Vector3d lastPosition;

    /**
     * last velocity of a given celestial object
     */
    private Vector3d lastVelocity;

    /**
     * current position of a given celestial object
     */
    private Vector3d currentPosition;

    /**
     * current velocity of a given celestial object
     */
    private Vector3d currentVelocity;

    /**
     * HashMap storing past positions of celestial objects
     */
    private HashMap<Integer, Vector3d> historicPositions;

    /**
     * HashMap storing past velocities of celestial objects
     */
    private HashMap<Integer, Vector3d> historicVelocities;

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
    public CelestialObject(String name, double m, Vector3d initialPos, Vector3d initialVel, double diameter, Color color, int radius) {
        historicPositions = new HashMap<>();
        historicVelocities = new HashMap<>();
        this.m = m;
        this.currentPosition = initialPos;
        this.lastPosition = initialPos;
        this.currentVelocity = initialVel;
        this.lastVelocity = initialVel;
        this.name = name;
        this.diameter = diameter;
        this.color = color;
        this.radius = radius;
    }


    public CelestialObject(String name, double m, Vector3d initialPos, Vector3d initialVel, long diameter, Color color) {
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
    public void updateCurrentPosition(Vector3d position) {
        currentPosition = position;
        if (Titan.currentStep % (86400 / Titan.stepSize) == 0) {
            historicPositions.put(Titan.currentStep, position);
        }
    }

    /**
     * updates the last position of a celestial body
     */
    public void updateLastPosition() {
        lastPosition = currentPosition;
    }

    /**
     * updates the current velocity of a celestial body
     * @param velocity
     */
    public void updateVelocity(Vector3d velocity) {
        currentVelocity = velocity;
        if (Titan.currentStep % (86400 / Titan.stepSize) == 0) {
            historicVelocities.put(Titan.currentStep, velocity);
        }
    }

    /**
     * updates the last velocity of a celestial body
     */
    public void updateLastVelocity() {
        lastVelocity = currentVelocity;
    }

    /**
     * returns the last position
     * @return
     */
    public Vector3d getLastPosition() {
        return lastPosition;
    }

    /**
     * returns the last velocity
     * @return
     */
    public Vector3d getLastVelocity() {
        return lastVelocity;
    }

    /**
     * returns the HashMap containing past positions of celestial objects
     * @return
     */
    public HashMap<Integer, Vector3d> getHistoricPositions() {
        return historicPositions;
    }

    /**
     * sorts the elements of the hashmap and returns them as an arraylist
     * @return
     */
    public ArrayList<Vector3d> getHistoricPositionsVector() {
        HashMap<Integer, Vector3d> positions = getHistoricPositions();
        ArrayList<Integer> sortedKeys = new ArrayList<>(positions.keySet());
        Collections.sort(sortedKeys);
        ArrayList<Vector3d> result = new ArrayList<>();
        for (Integer i : sortedKeys) {
            result.add(positions.get(i));
        }
        return result;
    }

    /*
    public Vector[] getLastPositionAndVelocity() {
        Vector[] a = new Vector[2];
        a[0] = getLastPosition();
        a[1] = getLastVelocity();
        return a;
    }
    public Vector[] getPositionAndVelocity(int index) {
        Vector[] a = new Vector[2];
        a[0] = getPosition(index);
        a[1] = getVelocity(index);
        return a;
    } */

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
