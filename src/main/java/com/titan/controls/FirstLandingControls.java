package com.titan.controls;

import com.titan.math.Vector;
import com.titan.model.LandingModule;

import java.util.Locale;

public class FirstLandingControls extends LandingControls {

    private double maxSpeed = 0;

    @Override
    public void execute(LandingModule module, int currentStep) {

        if (module.getTotalSpeed() > maxSpeed) maxSpeed = module.getTotalSpeed();

        System.out.println("---------");
        Vector thrust = new Vector(new double[]{0, 0});
        double rotationThrust = 0;
        double mainThrust = 0;

        double angle = directionOfAngle(module) * Math.toDegrees(angleVelocityTarget(module));
        System.out.printf(
                Locale.ENGLISH,
                "angle: %.2f° %n",
                angle);

        double angleDifference = module.getRotationAngle()-angle;

        System.out.printf(
                Locale.ENGLISH,
                "diff.: %.2f° %n",
                angleDifference);

        double rotationVelocity = module.getVelocity().getValue(2);

        if (angleDifference < -0.5 && rotationVelocity < 1) rotationThrust = 1;
        else if (angleDifference < -90 && rotationVelocity < 2) rotationThrust = 1;
        else if (angleDifference > 0.5 && rotationVelocity > -1) rotationThrust = -1;
        else if (angleDifference > 90 && rotationVelocity > -2) rotationThrust = -1;

        System.out.println("angle velocity: " + module.getVelocity().getValue(2) + " (+ (" + rotationThrust + "))");

        if (module.getY() < 50) {
            double totalSpeed = new Vector(new double[]{
                    module.getVelocity().getValue(0),
                    module.getVelocity().getValue(1)}).getLength();

            if (totalSpeed >= 1.0/1000) {
                if (angle > 90) mainThrust = totalSpeed/2;
                else mainThrust = totalSpeed/5;
            }
        }
        System.out.println("Thrust: " + mainThrust);

        module.setThrust(new Vector(new double[]{mainThrust, rotationThrust}));
    }

    /**
     * Is the current velocity vector on the left- or right-hand-side of the vector [0,0]-[landing-module]? <br>
     * -> rotation of the module + or - the angle calculated in angleVelocityTarget() <p>
     * The line/vector "[0,0]-[landing-module]" is modeled by the function y=ax+b, where b = 0 (because of [0,0])
     * and y and x is the position of the module. This leads to a=y/x. <br>
     * If we apply this function to the velocity, a*x_velocity is either larger
     * or smaller than the original y, hence it is either on the right- or the left-hand-side.
     */
    private int directionOfAngle(LandingModule module) {
        double a = module.getY()/module.getX();
        double ax_velocity = a * module.getVelocity().getValue(0);
        return (a * (module.getVelocity().getValue(1)-ax_velocity) < 0)? -1 : 1;
    }

    /**
     * Angle between the vector [0,0]-LandingModule and the vector of current velocity. <br>
     * Determines where to turn the landing module in order to land on [0,0]. <p>
     * Uses the function θ = acos( A.B / (|A|*|B|) )
     * @param module landing module
     * @return angle
     */
    private double angleVelocityTarget(LandingModule module) {
        Vector velocity = new Vector(new double[]{
                module.getVelocity().getValue(0),
                module.getVelocity().getValue(1)
        });
        Vector target = new Vector(new double[]{
                - module.getX(),
                - module.getY(),
        });

        double ADotBOverATimesB = velocity.dotProduct(target)
                / (velocity.getLength()*target.getLength());

        return Math.acos(ADotBOverATimesB);
    }

    private double timeUntilModuleReachesGround(LandingModule module) {
        return module.getX() / module.getVelocity().getValue(1);
    }

    private double speedToReach0XInTime(LandingModule module) {
        return 0;
    }
}
