package com.titan.controls;

import com.titan.model.Rocket;
import com.titan.model.SolarSystem;


/**
 * abstract class to implement custom controls for e.g. the rocket during the run of the simulation.<br>
 * Controls logging as well.
 * Technically every aspect of the system can be accessed and altered.
 */
public abstract class Controls {
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
    public Controls() {
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
     * @param system 
     * @param rocket
     * @param currentStep
     * @param stepSize
     */
    public abstract void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize);
}
