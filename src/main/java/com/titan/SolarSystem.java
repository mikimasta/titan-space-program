package com.titan;

import com.titan.math.EulerSolver;
import com.titan.math.GravitationFunction;
import com.titan.math.Vector;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SolarSystem {

    /**
     * contains all the celestialObjects
     */
    private final ArrayList<CelestialObject> celestialObjects = new ArrayList<>();

    /**
     * returns the arraylist containing
     * @return
     */
    public ArrayList<CelestialObject> getCelestialObjects() {
        return celestialObjects;
    }

    /**
     * constructs our SolarSystem with all the planets plus Moon and Titan
     */
    public SolarSystem() {

        CelestialObject sun = new CelestialObject("Sun", 1.99e30, new Vector(new double[]{0.00e0, 0.00e0, 0.00e0}), new Vector(new double[]{0.00e0, 0.00e0, 0.00e0}), 1392700, Color.YELLOW, 20);
        CelestialObject mercury = new CelestialObject("Mercury", 3.30e23, new Vector(new double[]{7.83e06, 4.49e07, 2.87e06}), new Vector(new double[]{-5.75e01, 1.15e01, 6.22e00}), 4879, Color.DARKORANGE, 7);
        CelestialObject venus = new CelestialObject("Venus", 4.87e24, new Vector(new double[]{-2.82e07, 1.04e08, 3.01e06}), new Vector(new double[]{-3.40e01, -8.97e00, 1.84e00}), 12104, Color.ORANGERED, 9);
        CelestialObject earth = new CelestialObject("Earth", 5.97e24, new Vector(new double[]{-1.48e08, -2.78e07, 3.37e04}), new Vector(new double[]{5.05e00, -2.94e01, 1.71e-03}), 12742, Color.ROYALBLUE, 9);
        CelestialObject moon = new CelestialObject("Moon", 7.35e22, new Vector(new double[]{-1.48e08, -2.75e07, 7.02e04}), new Vector(new double[]{4.34e00, -3.00e01, -1.16e-02}), 3475, Color.DARKSLATEGRAY, 2);
        CelestialObject mars = new CelestialObject("Mars", 6.42e23, new Vector(new double[]{-1.59e08, 1.89e08, 7.87e06}), new Vector(new double[]{-1.77e01, -1.35e01, 1.52e-01}), 6779, Color.DARKRED, 6);
        CelestialObject jupiter = new CelestialObject("Jupiter", 1.90e27, new Vector(new double[]{6.93e08, 2.59e08, -1.66e07}), new Vector(new double[]{-4.71e00, 1.29e01, 5.22e-02}), 139820, Color.ORANGE, 17);
        CelestialObject saturn = new CelestialObject("Saturn", 5.68e26, new Vector(new double[]{1.25e09, -7.60e08, -3.67e07}), new Vector(new double[]{4.47e00, 8.24e00, -3.21e-01}), 116460, Color.BEIGE, 15);
        CelestialObject titan = new CelestialObject("Titan", 1.35e23, new Vector(new double[]{1.25e09, -7.61e08, -3.63e07}), new Vector(new double[]{9.00e00, 1.11e01, -2.25e00}), 5150, Color.HOTPINK, 2);
        CelestialObject neptune = new CelestialObject("Neptune", 1.02e26, new Vector(new double[]{4.45e09, -3.98e08, -9.45e07}), new Vector(new double[]{4.48e-01, 5.45e00, -1.23e-01}), 49244, Color.GREEN, 15);
        CelestialObject uranus = new CelestialObject("Uranus", 8.68e25, new Vector(new double[]{1.96e09, 2.19e09, -1.72e07}), new Vector(new double[]{-5.13e00, 4.22e00, 8.21e-02}), 50724, Color.LIGHTBLUE, 14);

//        CelestialObject rocket1 = launchRocketInSameDirectionAsEarth("Rocket 1", 15.5, 50000);
//        CelestialObject rocket2 = launchRocket("Quite close Rocket", new Vector(13, -12, 0), 50000);
//        CelestialObject rocket3 = launchRocket("Experiment Rocket (very close)", new Vector(12.5, -11.5, 0), 50000);
//        CelestialObject rocket4 = launchRocket("Test Rocket (hopefully close)", new Vector(38.515, -14.905, 0), 50000);
        CelestialObject rocket5 = launchRocket("new rocket 1", new Vector(new double[]{38.514248, -14.903261, 0}), 50000);
        CelestialObject rocket6 = launchRocket("new rocket 2", new Vector(new double[]{38.65346, -14.9056, -1.35354}), 50000);

        CelestialObject rocketFin = launchRocket("Experia 1", new Vector(new double[]{38.65346586, -14.90558291, -1.3535296}), 50000);

        celestialObjects.addAll(List.of(sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus));

        //celestialObjects.add(rocketFin);
    }

    /**
     * creates a rocket
     * @param name name of the rocket
     * @param velocity initialVelocity of the rocket
     * @param mass mass of the rocket
     * @return rocket that will be added to celestialObjects
     */
    public CelestialObject launchRocket(String name, Vector velocity, double mass) {
        Vector initialPosition = new Vector(new double[]{-1.48e08, -2.78e07, 3.37e04});
        Vector initialVelocity = new Vector(new double[]{5.05e00, -2.94e01, 1.71e-03});

        initialVelocity = initialVelocity.add(velocity);
        initialPosition = initialPosition.add(new Vector(new double[]{0, 0, 6370}));

        return new CelestialObject(name, mass, initialPosition, initialVelocity, 1, Color.SILVER, 2);
    }

    public CelestialObject launchRocketInSameDirectionAsEarth(String name, double speed, double mass) {
        Vector velocity = new Vector(new double[]{5.05e00, -2.94e01, 1.71e-03});
        velocity = velocity.multiplyByScalar(1.0/29.831).multiplyByScalar(speed);
        return launchRocket(name, velocity, mass);
    }

    public Vector getAllPositions() {
        double[] result = new double[getCelestialObjects().size() * 3];
        for (int i = 0; i < getCelestialObjects().size(); i++) {
            result[i*3] = getCelestialObjects().get(i).getPosition().getValues()[0];
            result[i*3 + 1] = getCelestialObjects().get(i).getPosition().getValues()[1];
            result[i*3 + 2] = getCelestialObjects().get(i).getPosition().getValues()[2];
        }
        return new Vector(result);
    }

    public Vector getAllVelocities() {
        double[] result = new double[getCelestialObjects().size() * 3];
        for (int i = 0; i < getCelestialObjects().size(); i++) {
            result[i*3] = getCelestialObjects().get(i).getVelocity().getValues()[0];
            result[i*3 + 1] = getCelestialObjects().get(i).getVelocity().getValues()[1];
            result[i*3 + 2] = getCelestialObjects().get(i).getVelocity().getValues()[2];
        }
        return new Vector(result);
    }

    public Vector getAllMasses() {
        double[] result = new double[getCelestialObjects().size()];
        for (int i = 0; i < getCelestialObjects().size(); i++) {
            result[i] = getCelestialObjects().get(i).getM();
        }
        return new Vector(result);
    }

    public static void main(String[] args) {

        SolarSystem s = new SolarSystem();
        System.out.println(s.getCelestialObjects());

        for (CelestialObject o : s.celestialObjects) {
            System.out.println(o.getName() + "     " + o.getPosition() + " " + o.getVelocity() +  "       " + o.getM());
        }

        EulerSolver solver = new EulerSolver(60*60);
        for (int i = 0; i <= 365*24; i++) {
            Vector[] nextState = solver.solve(
                    new GravitationFunction(),
                    s.getAllPositions(),
                    s.getAllVelocities(),
                    s.getAllMasses(),
                    i);
            s.setAllPositions(nextState[0]);
            s.setAllVelocities(nextState[1]);
        }

        System.out.println("---------------------------------------------");
        for (CelestialObject o : s.celestialObjects) {
            System.out.println(o.getName() + "     " + o.getPosition() + " " + o.getVelocity());
        }

    }

    public void setAllPositions(Vector vector) {
        for (int i = 0; i < getCelestialObjects().size(); i++) {
            Vector position = new Vector(new double[]{
                    vector.getValues()[i*3],
                    vector.getValues()[i*3 + 1],
                    vector.getValues()[i*3 + 2]}
            );
            getCelestialObjects().get(i).updatePosition(position);
        }
    }

    public void setAllVelocities(Vector vector) {
        for (int i = 0; i < getCelestialObjects().size(); i++) {
            Vector velocity = new Vector(new double[]{
                    vector.getValues()[i*3],
                    vector.getValues()[i*3 + 1],
                    vector.getValues()[i*3 + 2]}
            );
            getCelestialObjects().get(i).updateVelocity(velocity);
        }
    }
}
