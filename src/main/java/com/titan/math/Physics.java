package com.titan.math;

import com.titan.CelestialObject;

import java.util.ArrayList;

public abstract class Physics {

    /**
     * gravitational constant
     */
    private static final double G = 6.6743e-20;

    /**
     * calculates the gravitational force on a given celestial objects by iterating through all the celestial objects and adding the force accordingly
     * @param currentPosition
     * @param mass
     * @param celestialObjects
     * @return
     */
    public static Vector3d gravitationalForce(Vector3d currentPosition, double mass, ArrayList<CelestialObject> celestialObjects) {

        Vector3d force = new Vector3d(0, 0, 0);
        for (CelestialObject o : celestialObjects) {

            if (!(currentPosition.subtract(o.getLastPosition()).getLength() == 0)) {

                Vector3d forceToAdd = currentPosition.subtract(o.getLastPosition().clone());

                double constant = (o.getM() * mass * G) / Math.pow(forceToAdd.getLength(), 3);

                force = force.add(forceToAdd.multiplyByScalar(constant));
            }
        }


        return force.multiplyByScalar(-1);
    }
}
