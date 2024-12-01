package io.github.cakilgan.spacelogger.system;

import io.github.cakilgan.spacelogger.SpaceLogger;
import io.github.cakilgan.spacelogger.serial.SpaceLoggerID;

import java.util.concurrent.ConcurrentHashMap;

public class LoggerSystem {
    private static ConcurrentHashMap<SpaceLoggerID, SpaceLogger> LOGGERS = new ConcurrentHashMap<>();

    public static SpaceLogger getOrNew(SpaceLoggerID id) {
        if (LOGGERS.containsKey(id)){
            return LOGGERS.get(id);
        }else{
            LOGGERS.put(id,new SpaceLogger(id));
            return LOGGERS.get(id);
        }
    }
}
