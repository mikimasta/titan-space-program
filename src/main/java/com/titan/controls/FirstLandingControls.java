package com.titan.controls;

import com.titan.math.Vector;
import com.titan.math.function.LandingGravitationFunction;
import com.titan.model.LandingModule;

public class FirstLandingControls extends LandingControls {

    private double maxDistance = -1;
    private double maxSpeed = -1;
    private double engineHeight = 100;
    private double desiredSpeed = 100;

    @Override
    public void execute(LandingModule module, int currentStep, double stepSize) {

        //if (module.getTotalSpeed() > maxSpeed) maxSpeed = module.getTotalSpeed();

        // System.out.println("---------");
        double rotationThrust = 0;
        double mainThrust = 0;

        double angle = directionOfAngle(module) * Math.toDegrees(angleVelocityTarget(module));
        // System.out.printf(
        //         Locale.ENGLISH,
        //         "angle: %.2f° %n",
        //         angle);

        double angleDifference = module.getRotationAngle()-angle;

        // System.out.printf(
        //         Locale.ENGLISH,
        //         "diff.: %.2f° %n",
        //         angleDifference);

        double rotationVelocity = module.getVelocity().getValue(2);

        if (angleDifference < -0.5 && rotationVelocity < 1) rotationThrust = 1;
        else if (angleDifference < -90 && rotationVelocity < 2) rotationThrust = 1;
        else if (angleDifference > 0.5 && rotationVelocity > -1) rotationThrust = -1;
        else if (angleDifference > 90 && rotationVelocity > -2) rotationThrust = -1;

        //System.out.println("angle velocity: " + module.getVelocity().getValue(2) + " (+ (" + rotationThrust + "))");

        mainThrust = thrustToReach0X0YInTime(module, angle);
        //System.out.println("Thrust: " + mainThrust);

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
                - module.getY()  ,
        });

        double ADotBOverATimesB = velocity.dotProduct(target)
                / (velocity.getLength()*target.getLength());

        return Math.acos(ADotBOverATimesB);
    }

    private double thrustToReach0X0YInTime(LandingModule module, double angle) {

        if (module.getVelocity().getValue(1)>0) return 0;
        if (module.getY() < 0.07) return slowDown(module);

        if (module.getY() > 150) return 0;

        Vector lineTargetUp = new Vector(new double[]{0, 1});
        Vector modulePosition = new Vector(new double[]{module.getX(), module.getY()});
        double angleTarget = Math.acos(lineTargetUp.dotProduct(modulePosition)
                        / (lineTargetUp.getLength() * modulePosition.getLength()));

        //return module.getTotalSpeed() * 2 * Math.sin(Math.toRadians(angle)/2);

        double thrust = module.getTotalSpeed() * ( - Math.cos(Math.toRadians(angle)) + 1);
        thrust = module.getTotalSpeed() * 2 * Math.sin(Math.toRadians(angle)/2);
       // thrust = Math.abs(module.getTotalSpeed() * 2 * Math.sin(Math.toRadians(angle)/2));
        System.out.println("---------");
        System.out.println("angle:  " + angle);
        System.out.println("speed:  " + module.getTotalSpeed());
        System.out.println("thrust    : " + thrust);
        System.out.println("thrust abs: " + Math.abs(thrust));


//        return Math.min(0.1, Math.abs(thrust));
        return Math.abs(thrust);

    }

    private double slowDown(LandingModule module) {
        if (module.getTotalSpeed() < 0.0001) return LandingGravitationFunction.GRAVITATIONAL_ACCELERATION * 0.9;

        double result = module.getTotalSpeed()/4 + LandingGravitationFunction.GRAVITATIONAL_ACCELERATION * 0.9;

        System.out.println("Slow down: " + result);

        return result;
    }
}
