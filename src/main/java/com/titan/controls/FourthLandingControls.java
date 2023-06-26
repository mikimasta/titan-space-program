package com.titan.controls;

import com.titan.math.Vector;
import com.titan.math.function.LandingGravitationFunction;
import com.titan.model.LandingModule;

public class FourthLandingControls extends LandingControls {

    private boolean landing = false;

    private boolean aligned = false;

    private double lastRotationVelocity = 0;

    @Override
    public void execute(LandingModule module, int currentStep, double stepSize) {
        module.setThrust(new Vector(new double[]{0, 0}));

        double rotationThrust;
        double mainThrust;

        Vector target = new Vector(new double[]{0,40});
        if (module.getY() < 42) target = new Vector(new double[]{0,20});
        if (module.getY() < 21) target = new Vector(new double[]{0,5});
        if (module.getY() < 5.2) target = new Vector(new double[]{0,0.1});
        if (module.getY() < 0.11) target = new Vector(new double[]{0,-1});
        if (module.getY() < 0.05) target = new Vector(new double[]{0,-1});
        if (module.getY() < 0.01 || landing) {
            landing = true;
            land(module);
            return;
        }

        double angle = directionOfAngleTarget(module, target) * Math.toDegrees(angleVelocityTarget(module, target));

        if (module.getY() < 0.05) angle = 0;
        if (aligned) angle = 0;

        rotationThrust = rotationThrustToReachAngle(angle, module);


        mainThrust = calculateThrust(module, angle);

        module.setThrust(new Vector(new double[]{mainThrust, rotationThrust}));
    }

    private void land(LandingModule module) {
        if (module.getVelocity().getValue(1)>0) return;

        double angle = 0;
        double yVelocity = Math.abs(module.getVelocity().getValue(1));
        //if (yVelocity < 0.0003) return LandingGravitationFunction.GRAVITATIONAL_ACCELERATION * 0.95;

        double rotationThrust = rotationThrustToReachAngle(angle, module);
        double mainThrust = LandingGravitationFunction.GRAVITATIONAL_ACCELERATION*0.5;
        if (module.getTotalSpeed() > 0.0003) {
            mainThrust = yVelocity/3.5 + LandingGravitationFunction.GRAVITATIONAL_ACCELERATION*0.95;
            if (module.getY() < 0.0002) {
                mainThrust = yVelocity + LandingGravitationFunction.GRAVITATIONAL_ACCELERATION;
            }
        }


        module.setThrust(new Vector(new double[]{mainThrust, rotationThrust}));
    }

    private double rotationThrustToReachAngle(double desiredAngle, LandingModule module) {
        double rotationThrust = 0;
        double angleDifference = module.getRotationAngle()-desiredAngle;

        double rotationVelocity = module.getVelocity().getValue(2);

        if (angleDifference < -0.5 && rotationVelocity < 1) rotationThrust = 1;
        else if (angleDifference < -90 && rotationVelocity < 2) rotationThrust = 1;
        else if (angleDifference > 0.5 && rotationVelocity > -1) rotationThrust = -1;
        else if (angleDifference > 90 && rotationVelocity > -2) rotationThrust = -1;

        lastRotationVelocity = rotationVelocity;
        return rotationThrust;
    }

    /**
     * Is the current velocity vector on the left- or right-hand-side of the vector [0,0]-[landing-module]? <br>
     * -> rotation of the module + or - the angle calculated in angleVelocityTarget() <p>
     * The line/vector "[target]-[landing-module]" is modeled by the function y=ax+b, where b = 0 (because of [0,0])
     * and y and x is the position of the module. This leads to a=y/x. <br>
     * If we apply this function to the velocity, a*x_velocity is either larger
     * or smaller than the original y, hence it is either on the right- or the left-hand-side.
     */
    private int directionOfAngleTarget(LandingModule module, Vector target) {
        double a = (module.getY() - target.getValue(1)) / (module.getX() - target.getValue(0));
        double ax_velocity = a * module.getVelocity().getValue(0);
        return (a * (module.getVelocity().getValue(1)-ax_velocity) < 0)? -1 : 1;
    }

    /**
     * Angle between the vector [target]-[LandingModule] and the vector of current velocity. <br>
     * Determines where to turn the landing module in order to land on [0,0]. <p>
     * Uses the function θ = acos( A.B / (|A|*|B|) )
     * @param module landing module
     * @return angle
     */
    private double angleVelocityTarget(LandingModule module, Vector target) {
        Vector velocity = new Vector(new double[]{
                module.getVelocity().getValue(0),
                module.getVelocity().getValue(1)
        });
        Vector modulePosition = new Vector(new double[]{
                module.getX(),
                module.getY()
        });
        target = target.subtract(modulePosition);

        double ADotBOverATimesB = velocity.dotProduct(target)
                / (velocity.getLength()*target.getLength());

        return Math.acos(ADotBOverATimesB);
    }

    private double calculateThrust(LandingModule module, double angle) {

        if (module.getVelocity().getValue(1)>0) return 0;
        if (module.getY() < 0.04) return slowDown(module);

        if (module.getY() > 150) return 0;

        double thrust = module.getTotalSpeed() * 2 * Math.sin(Math.toRadians(angle)/2);

        return Math.abs(thrust);
    }

    private double slowDown(LandingModule module) {
        if (module.getVelocity().getValue(1)>0) return 0;

        double yVelocity = Math.abs(module.getVelocity().getValue(1));
        if (yVelocity < 0.0003) return LandingGravitationFunction.GRAVITATIONAL_ACCELERATION * 0.95;

        if (module.getY() < 0.025) return yVelocity + LandingGravitationFunction.GRAVITATIONAL_ACCELERATION * 0.95;

        double result = yVelocity/2.5 + LandingGravitationFunction.GRAVITATIONAL_ACCELERATION * 0.95;

        return result;
    }
}
