package io.github.cakilgan.spacelogger.core;

public enum LoggerLevel {
    LIFECYCLE("LIFE-CYCLE", 53),
    INFO("INFO",40),
    DEBUG("DEBUG",21),
    WARNING("WARNING",136),
    ERROR("ERROR",196),
    EXCEPTION("EXCEPTION",52),
    TRACE("TRACE",56);
    String format;
    int rgb256;
    LoggerLevel(String format,int rgb256){
        this.format = format;
        this.rgb256 = rgb256;
    }
    public String getFormat() {
        return format;
    }
    public int getRgb256() {
        return rgb256;
    }

    public static LoggerLevel parse(String cx){
        for (LoggerLevel value : LoggerLevel.values()) {
            if (value.format.equalsIgnoreCase(cx)){
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
}
