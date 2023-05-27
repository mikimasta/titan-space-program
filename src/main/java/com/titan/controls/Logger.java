package com.titan.controls;


/**
 * This interface acts as a logger and its used to gather data during the mission
 */
public interface Logger {
        
    void log(Object messageToLog);

    boolean isLogging();

    String getLog();

}
