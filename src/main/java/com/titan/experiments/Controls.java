package com.titan.experiments;

import com.titan.model.Rocket;
import com.titan.model.SolarSystem;

/**
 * interface to implement custom controls for e.g. the rocket during the run of the simulation.
 * Technically every aspect of the system can be accessed and altered.
 */
public interface Controls {

    void execute(SolarSystem system, Rocket rocket, int currentStep, int stepSize);
}
