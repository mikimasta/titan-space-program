package com.titan.controls;

import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

/**
 * interface to implement custom controls for e.g. the rocket during the run of the simulation.
 * Technically every aspect of the system can be accessed and altered.
 */
public abstract class Controls {
    
    protected boolean logging = true;

    public abstract void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize);
}
