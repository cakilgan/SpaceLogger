package io.github.cakilgan.spacelogger.core;

import io.github.cakilgan.spacelogger.io.LogChannelProcessor;

import java.util.UUID;

public abstract class AbstractLogger implements ILogger{
    private LogFormat FORMAT;
    public LogFormat format() {
        return FORMAT;
    }
    public void setFormat(LogFormat FORMAT) {
        this.FORMAT = FORMAT;
    }
    private LogChannelProcessor PROCESSOR;
    public LogChannelProcessor processor() {
        return PROCESSOR;
    }
    public void setProcessor(LogChannelProcessor PROCESSOR) {
        this.PROCESSOR = PROCESSOR;
    }
    protected AbstractLogger(){
        setProcessor(null);
        setFormat(new DefaultLogFormat());
    }
    protected AbstractLogger(UUID uuid){
        setProcessor(LogChannelProcessor.getOrNew(uuid));
        setFormat(new DefaultLogFormat());
    }
    protected AbstractLogger(UUID uuid,LogFormat format){
        setProcessor(LogChannelProcessor.getOrNew(uuid));
        setFormat(format);
    }
}
