package com.titan.experiments;

import com.titan.Simulation;
import com.titan.gui.Titan;
import com.titan.math.Vector;

/**
 * Abstract class that can be extended to implement custom versions of
 * a hill-climbing algorithm to compute a vector (e.g. velocity, force or position).
 * <p>
 * The two methods {@link #setUpSimulation(Vector, int)} and {@link #calculateError(Simulation)} have to be
 * overridden in the custom implementation. They are the parts of the algorithm which are responsible for
 * the custom use of the input vector in the simulation and the error-value that the
 * hill-climbing algorithm tries to minimize.
 */
public abstract class HillClimbing {

    /** seconds in a year (default value, override in a constructor if needed
     * see {@link CalculateReturningVelocity_HillClimbing#CalculateReturningVelocity_HillClimbing()}) */
    int secondsUntilResultsAreCompared = 31536000;

    class Result {
        public Vector input;
        public double output;

        public Result(Vector input, double output) {
            this.input = input;
            this.output = output;
        }
    }

    /**
     * Has to be overridden in the extensions of this class. <p>
     * Contains code that initializes a Simulation that uses the input Vector (e.g. by passing
     * it into a custom Control-instance that handles it during runtime)
     * @param input input Vector that the hill-climbing algorithm optimizes
     * @param stepSize step-size of the Simulation
     * @return initialized Simulation.
     */
    abstract Simulation setUpSimulation(Vector input, int stepSize);

    /**
     * Has to be overridden in the extensions of this class. <p>
     * Calculates the error the hill-climbing algorithm is trying to minimize. (e.g. distance
     * between two objects or fuel consumed) <br>
     * Has access to every parameter of the simulation.
     * @param simulation simulation the error is calculated for
     * @return the calculated error
     */
    abstract double calculateError(Simulation simulation);

    /**
     * Climbs the hill with the given climbingStepSize until no step in any direction improves the error anymore.
     * The method {@link #climbOnceInOneDirection(Vector, double, Vector, double)} is called six times: <br>
     * - three times with vectors that span R3 (the space of the simulation (x, y, z)) <br>
     * - another three times in the negative direction of the first vectors <br>
     *
     * @param currentBest current vector that achieved the last best result. This is the vector that will be altered
     *                    during the method to test new values to find a better input.
     * @param currentError current best error - achieved by the vector of currentBest
     * @param climbingStepSize size of the steps the algorithm takes in each direction
     * @return the new (or old if nothing changed) best result
     */
    private Result climbTheHill(Vector currentBest, double currentError, double climbingStepSize) {
        Result result = new Result(currentBest, currentError);

        boolean complete = false;
        while (!complete) {
            complete = true;

            // z (only 0.5 since z does not vary that much)
            result = climbOnceInOneDirection(result.input, result.output, new Vector(new double[]{0, 0, 0.5}), climbingStepSize);
            if (result.input.equals(currentBest)) {
                result = climbOnceInOneDirection(result.input, result.output, new Vector(new double[]{0, 0, -0.5}), climbingStepSize);
            }
            if (!result.input.equals(currentBest)) {
                complete = false;
                currentBest = result.input;
            }

            // y
            result = climbOnceInOneDirection(result.input, result.output, new Vector(new double[]{0, 1, 0}), climbingStepSize);
            if (result.input.equals(currentBest)) {
                result = climbOnceInOneDirection(result.input, result.output, new Vector(new double[]{0, -1, 0}), climbingStepSize);
            }
            if (!result.input.equals(currentBest)) {
                complete = false;
                currentBest = result.input;
            }

            // x
            result = climbOnceInOneDirection(result.input, result.output, new Vector(new double[]{1, 0, 0}), climbingStepSize);
            if (result.input.equals(currentBest)) {
                result = climbOnceInOneDirection(result.input, result.output, new Vector(new double[]{-1, 0, 0}), climbingStepSize);
            }
            if (!result.input.equals(currentBest)) {
                complete = false;
                currentBest = result.input;
            }
        }

        System.out.println("# current best: " + result.output + " with input: " + result.input);
        return new Result(currentBest, result.output);
    }

    /**
     * Takes one step in the given direction and returns either the new optimized result or the old one, if
     * the step did not improve the error.
     * @param input current best input Vector
     * @param error current best error
     * @param climbingDirection Vector of the climbing direction
     * @param climbingStepSize step-size the algorithm takes
     * @return new (or old) best result
     */
    private Result climbOnceInOneDirection(Vector input, double error, Vector climbingDirection, double climbingStepSize) {
        Vector newVelocity = input.add(climbingDirection.multiplyByScalar(climbingStepSize));
        System.out.print("Test with input: " + newVelocity);
        Simulation simulation = setUpSimulation(newVelocity, 60);
        Simulation.runFor(simulation, secondsUntilResultsAreCompared, true);
        System.out.println();
        if (calculateError(simulation) < error) {
            error = calculateError(simulation);
            input = newVelocity;
            System.out.println("new best: " + (int) error);
        }
        return new Result(input, error);
    }

    /**
     * Runs the hill-climbing algorithm until a specified precision.
     * @param startingInput
     * @param stepSize
     * @param accuracy
     * @return
     */
    public Result run(Vector startingInput, int stepSize, int accuracy) {
        Titan.currentStep = 1;
        Simulation simulation = setUpSimulation(startingInput, stepSize);
        System.out.print("\nRun with initial condition: " + startingInput);
        Simulation.runFor(simulation, secondsUntilResultsAreCompared, true);
        Result result = new Result(startingInput, calculateError(simulation));
        System.out.println("\n# current best: " + result.output + " with input: " + result.input);

        for (int i = 0; i < accuracy; i++) {
            double climbingStepSize = 10.0/Math.pow(2, i);
            System.out.println("next climbing step size: " + climbingStepSize + " (10 / 2^" + i + ")");
            result = climbTheHill(result.input, result.output, climbingStepSize);
        }
        return result;
    }
}
