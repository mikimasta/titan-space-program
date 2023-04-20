package com.titan;

import java.util.ArrayList;

/**
 *
 */
public class EulerSolver {

    /**
     * determines the step size for the Euler solver
     */
    private double step;

    /**
     * gravitational constant
     */
    private static final double G = 6.6743e-20;

    /**
     * Constructs an Euler solver and sets the step size
     * @param step
     */
    public EulerSolver(int step) {
        this.step = step;
    }

    /**
     * returns the step size
     * @return
     */
    public double getStep() {
        return step;
    }

    /**
     * sets the step size
     * @param step
     */
    public void setStep(double step) {
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
                Vector[] nextState = nextState(o.getLastPosition(), o.getLastVelocity(), o.getM(), s.getCelestialObjects(), currentStep);
                o.updateCurrentPosition(nextState[0]);
                o.updateVelocity(nextState[1]);

            } else {
                o.updateVelocity(new Vector(0, 0, 0));
                o.updateCurrentPosition(new Vector(0, 0, 0));
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
    public Vector[] nextState(Vector currentPosition, Vector currentVelocity, double mass, ArrayList<CelestialObject> celestialObjects, int currentStep) {

        Vector[] resultOfDiffEquation = differentialEquation(currentPosition, currentVelocity, mass, celestialObjects, currentStep);

        Vector nextPosition = currentPosition.add(resultOfDiffEquation[0].multiplyByScalar(step));
        Vector nextVelocity = currentVelocity.add(resultOfDiffEquation[1].multiplyByScalar(step));

        Vector[] newState = new Vector[2];

        newState[0] = nextPosition;
        newState[1] = nextVelocity;

        return newState;
    }


    /**
     * calculates the gravitational force on a given celestial objects by iterating through all the celestial objects and adding the force accordingly
     * @param currentPosition
     * @param mass
     * @param celestialObjects
     * @param currentStep
     * @return
     */
    private Vector gravitationalForce(Vector currentPosition, double mass, ArrayList<CelestialObject> celestialObjects, int currentStep) {

        Vector force = new Vector(0, 0, 0);
        for (CelestialObject o : celestialObjects) {

            if (!(currentPosition.subtract(o.getLastPosition()).getLength() == 0)) {

                Vector forceToAdd = currentPosition.subtract(o.getLastPosition().clone());

                double constant = (o.getM() * mass * G) / Math.pow(forceToAdd.getLength(), 3);

                force = force.add(forceToAdd.multiplyByScalar(constant));
            }
        }


        return force.multiplyByScalar(-1);
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
    private Vector[] differentialEquation(Vector currentPosition, Vector currentVelocity, double mass, ArrayList<CelestialObject> celestialObjects, int currentStep) {

        Vector g = gravitationalForce(currentPosition, mass, celestialObjects, currentStep).multiplyByScalar(1.0/mass);

        Vector[] res = new Vector[2];
        res[0] = currentVelocity;
        res[1] = g;

        return res;
    }


}
