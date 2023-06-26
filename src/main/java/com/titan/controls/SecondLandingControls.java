package com.titan.controls;

import com.titan.math.Vector;
import com.titan.model.LandingModule;

public class SecondLandingControls extends LandingControls {

    private double desiredAngle = 0;
    private Vector firstTarget = new Vector(new double[]{50, 0});
    private Vector secondTarget = new Vector(new double[]{10, 0});
    private Vector finalTarget = new Vector(new double[]{2, 0});

    private int state = 0;

    @Override
    public void execute(LandingModule module, int currentStep, double stepSize) {


        if (state == 0) {
            state = 1;

            return;
        }

        if (state == 1) {
            double angle = angleVelocityTarget(module, firstTarget) * directionOfAngle(module, firstTarget);

        }


        double angleTarget = angleVelocityTarget(module);

        if (module.getVelocity().getValue(1) > 0) return;

        if (angleTarget > 30) {
            moveToCenter(module);
            return;
        }

        double rotationThrust = 0;
        double mainThrust = 0;

        double angle = directionOfAngle(module) * Math.toDegrees(angleVelocityTarget(module));
        desiredAngle = angle;

        double angleDifference = module.getRotationAngle()-angle;

        double rotationVelocity = module.getVelocity().getValue(2);

        if (angleDifference < -0.5 && rotationVelocity < 1) rotationThrust = 1;
        else if (angleDifference < -90 && rotationVelocity < 2) rotationThrust = 1;
        else if (angleDifference > 0.5 && rotationVelocity > -1) rotationThrust = -1;
        else if (angleDifference > 90 && rotationVelocity > -2) rotationThrust = -1;

        //System.out.println("angle velocity: " + module.getVelocity().getValue(2) + " (+ (" + rotationThrust + "))");

        mainThrust = thrustToReach0X0YInTime(module, angle, stepSize);
        //System.out.println("Thrust: " + mainThrust);

        module.setThrust(new Vector(new double[]{mainThrust, rotationThrust}));
    }

    private void moveToCenter(LandingModule module) {
        double desiredRotation = 180;
        if (module.getX() < 0) desiredRotation *= -1;

        double rotationThrust = 0;
        double mainThrust = 0;

        double angleDifference = module.getRotationAngle()-desiredRotation;
        double rotationVelocity = module.getVelocity().getValue(2);

        if (angleDifference < -0.5 && rotationVelocity < 1) rotationThrust = 1;
        else if (angleDifference < -90 && rotationVelocity < 2) rotationThrust = 1;
        else if (angleDifference > 0.5 && rotationVelocity > -1) rotationThrust = -1;
        else if (angleDifference > 90 && rotationVelocity > -2) rotationThrust = -1;


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

    private int directionOfAngle(LandingModule module, Vector v) {
        double a = (module.getY() - v.getValue(0)) / (module.getX() - v.getValue(1));
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
                - module.getY() + 1,
        });

        double ADotBOverATimesB = velocity.dotProduct(target)
                / (velocity.getLength()*target.getLength());

        return Math.acos(ADotBOverATimesB);
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

    private double angleWithTarget(LandingModule module) {
        Vector lineTargetUp = new Vector(new double[]{0, 1});
        Vector modulePosition = new Vector(new double[]{module.getX(), module.getY()});

        return Math.acos(lineTargetUp.dotProduct(modulePosition)
                / (lineTargetUp.getLength() * modulePosition.getLength()));
    }

    private double thrustToReach0X0YInTime(LandingModule module, double angle, double stepSize) {

        if (module.getY() > 150) return 0;
        if(Math.abs(desiredAngle - module.getRotationAngle()) > 5) return 0;

        double thrust = module.getTotalSpeed() * ( - Math.cos(Math.toRadians(angle)) + 1);
        //thrust = module.getTotalSpeed() * 2 * Math.sin(Math.toRadians(angle)/2);
       // thrust = Math.abs(module.getTotalSpeed() * 2 * Math.sin(Math.toRadians(angle)/2));
        System.out.println("---------");
        System.out.println("angle:  " + angle);
        System.out.println("speed:  " + module.getTotalSpeed());
        System.out.println("thrust    : " + thrust);
        System.out.println("thrust abs: " + Math.abs(thrust));

        double impulse = Math.abs(thrust) * 1000 * module.getM();
        double force = impulse / stepSize;
        System.out.println("Force: " + force );



//        return Math.min(0.1, Math.abs(thrust));
        return Math.min(0.01352, Math.abs(thrust));

    }
}
