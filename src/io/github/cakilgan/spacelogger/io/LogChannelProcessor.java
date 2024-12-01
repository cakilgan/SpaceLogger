package io.github.cakilgan.spacelogger.io;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class LogChannelProcessor {
    public static final ConcurrentHashMap<UUID,LogChannelProcessor> PROCESSORS = new ConcurrentHashMap<>();
    public static LogChannelProcessor getOrNew(UUID uuid){
        if (!PROCESSORS.containsKey(uuid)) {
            PROCESSORS.put(uuid, new LogChannelProcessor());
        }
        return PROCESSORS.get(uuid);
    }
    public static void forceAllProcessorsToStop(){
        PROCESSORS.forEach(new BiConsumer<UUID, LogChannelProcessor>() {
            @Override
            public void accept(UUID uuid, LogChannelProcessor logChannelProcessor) {
                logChannelProcessor.closeAll();
            }
        });
    }


    public List<LogChannel> CHANNELS;
    public LogChannelProcessor(){
        CHANNELS = new ArrayList<>();
    }
    public void startAll(){
        for (LogChannel channel : CHANNELS) {
            channel.startIfNotRunning();
            //TODO: log channel connect msg
        }
    }
    public void closeAll(){
        for (LogChannel channel : CHANNELS) {
            channel.close();
            //TODO: log channel disconnect msg
        }
    }
    public void pushAll(LogMessage<?> msg){
        for (LogChannel channel : CHANNELS) {
            channel.push(msg);
        }
    }
}
