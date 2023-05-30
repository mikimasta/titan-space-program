package com.titan.controls;

/**
 * Logs data related to spaceship velocitites, distances to key points, orbital data etc.
 */
public class MissionLogger implements Logger {

    /**
     * StringBuilder to append the logs to
     */
    private StringBuilder log;
    
    /**
     * determines whether the logger should be logging
     */
    private boolean logging = true;

    /**
     * Constructs a new MissionLogger(), instantiates the StringBuilder
     */
    public MissionLogger() {
        log = new StringBuilder();
    }
    
    /**
     * @return whether the logger is logging
     */
    @Override
    public boolean isLogging() {
        return logging;
    }
    
    /**
     * logs a message adding it to the StringBuilder
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
     * @return String representation of the log
     */
    @Override
    public String getLog() {
        return log.toString();
    }
    
    /**
     * sets the logging value
     * @param value
     */
    public void setLogging(boolean value) {
        this.logging = value;
    }
}
