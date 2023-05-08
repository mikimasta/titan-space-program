package com.titan.experiments;

import com.titan.CelestialObject;
import com.titan.SolarSystem;
import com.titan.math.EulerSolver;
import com.titan.math.GravitationFunction;
import com.titan.math.Vector;

import java.util.ArrayList;

/**
 * class that helps calculate the correct velocity of the probe for it to hit titan
 */
public class CalculateTrajectory {

    /**
     * step we are currently on
     */
    private static int currentStep;

    /**
     * velocity vector a probe has initially
     */
    private static Vector velocity = new Vector(new double[]{38.65346586, -14.90558291, -1.3535296});

    /**
     * previous smallest distance of the probe from Titan
     */
    private static double previousSmallestDistance = Double.MAX_VALUE;

    private static int  launchNumber = 1;

    /**
     * here the velocity is updated with every iteration of the complete solution of the solar system after a year. the distance to Titan on the 1st of April 2024 is calculated and
     * velocity is being changed accordingly
     * @param args
     */
    public static void main(String[] args) {


        while (true) {

            SolarSystem s = new SolarSystem();
            CelestialObject rocket = s.launchRocket("Experia " + launchNumber, velocity, 50000);
            s.getCelestialObjects().add(rocket);
            ArrayList<CelestialObject> obj = s.getCelestialObjects();
            currentStep = 0;


            while (currentStep < 365 * 24 * 60) {

                EulerSolver solver = new EulerSolver(60);

                for (int i = 0; i < 1; i++) {
                    Vector[] nextState = solver.solve(
                            new GravitationFunction(),
                            s.getAllPositions(),
                            s.getAllVelocities(),
                            s.getAllMasses(),
                            currentStep);
                    s.setAllPositions(nextState[0]);
                    s.setAllVelocities(nextState[1]);
                    currentStep++;
                }
            }
            double distToTitan = (obj.get(8).getPosition().subtract(rocket.getPosition())).getLength(); //- (obj.get(8).getDiameter() / 2);
            if (previousSmallestDistance > distToTitan || launchNumber % 100000 == 0) {

                previousSmallestDistance = distToTitan;
                System.out.println("Rocket name: " + rocket.getName());
                System.out.println("launchVelocity: " + velocity);
                System.out.println("currentVelocity: " + rocket.getVelocity());
                System.out.println("Speed: " + rocket.getVelocity().getLength() + " km/s");
                System.out.println("Distance to the center of Titan: " + distToTitan + " km" + "\n\n");

            }
            velocity = velocity.add(new Vector(new double[]{0.0, -0.00000001, 0.0}));
            launchNumber++;
        }
    }

}
