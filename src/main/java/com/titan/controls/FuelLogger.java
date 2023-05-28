package com.titan.controls;


public class FuelLogger implements Logger {

    private StringBuilder log;

    private boolean logging = true;

    public FuelLogger() {
        log = new StringBuilder();
    }

    @Override
    public boolean isLogging() {
        return logging;
    }

    @Override
    public void log(Object messageToLog) {
       log.append(messageToLog);
       log.append("\n");
       log.append("----------");
       log.append("\n");
    }

    @Override
    public String getLog() {
        return log.toString();
    }

    public void setLogging(boolean value) {
        this.logging = value;
    }
}
