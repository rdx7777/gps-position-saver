package com.github.rdx7777.gpspositionsaver.model.logger;

public enum LogType {

    TRACE("TRACE"), DEBUGGED("DEBUG"), INFO("INFO"), WARN("WARN"), ERROR("ERROR"), FATAL("FATAL");

    private final String name;

    LogType(String code) {
        this.name = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
