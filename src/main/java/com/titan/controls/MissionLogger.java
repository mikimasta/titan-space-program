package com.titan.controls;


public class MissionLogger implements Logger {

    private StringBuilder log;

    private boolean logging = true;

    public MissionLogger() {
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
    }

    @Override
    public String getLog() {
        return log.toString();
    }

    public void setLogging(boolean value) {
        this.logging = value;
    }
}
