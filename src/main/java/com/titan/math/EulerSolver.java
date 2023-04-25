package com.titan.math;

import com.titan.CelestialObject;
import com.titan.SolarSystem;

import java.util.ArrayList;

/**
 *
 */
public class EulerSolver implements Solver {

    /**
     * determines the step size for the Euler solver
     */
    private double step;

    /**
     * Constructs an Euler solver and sets the step size
     * @param step
     */
    public EulerSolver(int step) {
        this.step = step;
    }

    /**
     * this method calculates and updates the posistions and velocities of all celestial objects after every iteration using the explicit Euler method
     * @param s SolarSystem containing all the CelestialObjects
     * @param currentStep step we are currently on
     */
    public void solve(SolarSystem s, int currentStep) {

        for (CelestialObject o : s.getCelestialObjects()) {
            o.updateLastPosition();
            o.updateLastVelocity();
        }

        for (CelestialObject o : s.getCelestialObjects()) {
            if (!(o.getName().equals("Sun"))) {
                Vector3d[] nextState = nextState(o.getLastPosition(), o.getLastVelocity(), o.getM(), s.getCelestialObjects(), currentStep);
                o.updateCurrentPosition(nextState[0]);
                o.updateVelocity(nextState[1]);

            } else {
                o.updateVelocity(new Vector3d(0, 0, 0));
                o.updateCurrentPosition(new Vector3d(0, 0, 0));
            }
        }

    }

    /**
     * helper method for solve(), assigns the newly calculated values to a Vector[] array
     * @param currentPosition current position of a celestial object
     * @param currentVelocity current velocity of a celestial object
     * @param mass mass of the celestial body
     * @param celestialObjects contains all the CelestialObjects in the SolarSystem
     * @param currentStep step we are currently on
     * @return returns a Vector[] array contating the next posistion and velocity of a given celestial object
     */
    public Vector3d[] nextState(Vector3d currentPosition, Vector3d currentVelocity, double mass, ArrayList<CelestialObject> celestialObjects, int currentStep) {

        Vector3d[] resultOfDiffEquation = differentialEquation(currentPosition, currentVelocity, mass, celestialObjects, currentStep);

        Vector3d nextPosition = currentPosition.add(resultOfDiffEquation[0].multiplyByScalar(step));
        Vector3d nextVelocity = currentVelocity.add(resultOfDiffEquation[1].multiplyByScalar(step));

        Vector3d[] newState = new Vector3d[2];

        newState[0] = nextPosition;
        newState[1] = nextVelocity;

        return newState;
    }

    /**
     * helper method for solve(), stores the results of gravitational force in an array
     * @param currentPosition
     * @param currentVelocity
     * @param mass
     * @param celestialObjects
     * @param currentStep
     * @return Vector[] array containing the current velocity of a celestial object and the gravitational force it is afffected by
     */
    private Vector3d[] differentialEquation(Vector3d currentPosition, Vector3d currentVelocity, double mass, ArrayList<CelestialObject> celestialObjects, int currentStep) {

        Vector3d g = Physics.gravitationalForce(currentPosition, mass, celestialObjects).multiplyByScalar(1.0/mass);

        Vector3d[] res = new Vector3d[2];
        res[0] = currentVelocity;
        res[1] = g;

        return res;
    }


}
