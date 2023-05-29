package com.titan.controls;


public class EngineLogger implements Logger {

    private StringBuilder log;

    private boolean logging = true;

    public EngineLogger() {
        log = new StringBuilder();
    }

    @Override
    public boolean isLogging() {
        return logging;
    }

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

    @Override
    public String getLog() {
        return log.toString();
    }

    public void setLogging(boolean value) {
        this.logging = value;
    }
}
