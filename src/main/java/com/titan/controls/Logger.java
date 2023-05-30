package com.titan.controls;


/**
 * This interface acts as a logger and its used to gather data during the mission
 */
public interface Logger {
 
    /**
     * logs the message
     * @param messageToLog
     */
    void log(Object messageToLog);
    
    /**
     * checks if the logger is currently logging
     */
    boolean isLogging();
    
    /**
     * returns the log as a String
     */
    String getLog();

}
