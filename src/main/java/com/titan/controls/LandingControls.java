package com.titan.controls;

import com.titan.model.LandingModule;


/**
 * abstract class to implement custom landing-controls for e.g. the rocket during the run of the simulation.<br>
 * Controls logging as well.
 * Technically every aspect of the system can be accessed and altered.
 */
public abstract class LandingControls {
    /**
     * logs all data related to mission success like velocities, speeds and orbital data
     */
    protected Logger missionLogger;

    /**
     * logs data related to the engine and fuel consumption
     */
    protected Logger engineLogger;

    /**
     * constructs a Controls object and instantiates the loggers
     */
    public LandingControls() {
        missionLogger = new MissionLogger();
        engineLogger = new EngineLogger();
    }
    
    /**
     * @return returns the instance of the mission logger
     */
    public Logger getMissionLogger() {
        return missionLogger;
    }

    /**
     * @return returns the instance of the engine logger
     */
    public Logger getEngineLogger() {
        return engineLogger;
    }

    /**
     * Called with every step of the solving process 
     * @param module
     * @param currentStep
     */
    public abstract void execute(LandingModule module, int currentStep, double stepSize);
}
