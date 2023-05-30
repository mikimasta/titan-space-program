package com.titan.controls;

/**
 * Acts as a logger, logs data regarding the engine, fuel consumption and thrust
 */
public class EngineLogger implements Logger {

    /**
     * represents the log as a StringBuilder instance
     */
    private StringBuilder log;
    
    /**
     * determines whether the program logs the data or not
     */
    private boolean logging = true;

    /**
    * Creates an EngineLogger object and instantiates the StringBuilder object
    */
    public EngineLogger() {
        log = new StringBuilder();
    }

    /**
     * @return returns the value of logging
     */
    @Override
    public boolean isLogging() {
        return logging;
    }

    /**
     * logs a message and appends to the StringBuilder
     * @param messageToLog
     */
    @Override
    public void log(Object messageToLog) {
        if (isLogging()) {
            log.append(messageToLog);
            log.append("\n");
            log.append("----------");
            log.append("\n");

            // if the simulation is not running via javafx but just in the terminal
            System.out.print(messageToLog);
            System.out.print("\n");
            System.out.print("----------");
            System.out.print("\n");
        }
    }
    
    /**
     * @return the entire log thus far
     */
    @Override
    public String getLog() {
        return log.toString();
    }
    
    /**
     * sets the logging to value
     * @param value
     */
    public void setLogging(boolean value) {
        this.logging = value;
    }
}
