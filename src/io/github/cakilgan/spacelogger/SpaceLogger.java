package io.github.cakilgan.spacelogger;


import io.github.cakilgan.spacelogger.core.AbstractLogger;
import io.github.cakilgan.spacelogger.core.DefaultLogFormat;
import io.github.cakilgan.spacelogger.core.LogFormat;
import io.github.cakilgan.spacelogger.core.LoggerLevel;
import io.github.cakilgan.spacelogger.io.LogMessage;
import io.github.cakilgan.spacelogger.io.LogPriority;
import io.github.cakilgan.spacelogger.serial.SpaceLoggerID;
import io.github.cakilgan.spacelogger.util.ColorUtils;
import io.github.cakilgan.spacelogger.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class SpaceLogger extends AbstractLogger {
    public static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    public static class LogDetail{
        public Map<String,Object> detailMap = new HashMap<>();
        public static LogDetail of(String []keys,Object []values){
            LogDetail logDetail = new LogDetail();
            for (int i = 0; i < keys.length; i++) {
            logDetail.detailMap.put(keys[i],values[i]);
            }
            return logDetail;
        }
    }
    public static class LogEvent{
        String eventName;
        public LogEvent(String eventName) {
            this.eventName = eventName;
        }
    }

    public SpaceLogger(SpaceLoggerID id){
        super();
        LOGGER_ID = id;
    }
    public SpaceLogger(SpaceLoggerID id,UUID processorID){
        super(processorID);
        LOGGER_ID = id;
    }
    protected SpaceLogger(String name, UUID processorID) {
        super(processorID);
        LOGGER_ID = new SpaceLoggerID(name);
    }
    protected SpaceLogger(UUID uuid, LogFormat format) {
        super(uuid, format);
    }

    LogPriority currentLogPriority = LogPriority.TRASH;
    LoggerLevel currentLevel = LoggerLevel.INFO;
    LogDetail currentLogDetail = null;
    SpaceLoggerID LOGGER_ID;
    String thread = "";
    LogEvent newEvent;
    int maxFrames = 5;


    public SpaceLogger newEvent(String eventName){
        newEvent = new LogEvent(eventName);
        this.processor().pushAll(new LogMessage<>(DefaultLogFormat.DEFAULT_DATE_FORMAT.format(new Date())+" "+ColorUtils.color256(87,"START")+" EVENT "+newEvent.eventName+" {"));
        return this;
    }
    public SpaceLogger finishEvent(){
        this.processor().pushAll(new LogMessage<>("} "+DefaultLogFormat.DEFAULT_DATE_FORMAT.format(new Date())+" "+ColorUtils.color256(ColorUtils.BRIGHT_RED,"FINISH")+" EVENT "+newEvent.eventName));
        newEvent = null;
        return this;
    }

    public void setCurrentLevel(LoggerLevel currentLevel) {
        this.currentLevel = currentLevel;
    }
    public void setCurrentLogPriority(LogPriority currentLogPriority) {
        this.currentLogPriority = currentLogPriority;
    }
    public void setCurrentLogDetail(LogDetail currentLogDetail) {
        this.currentLogDetail = currentLogDetail;
    }
    public void setMaxFrames(int maxFrames) {
        this.maxFrames = maxFrames;
    }

    public SpaceLoggerID ID() {
        return LOGGER_ID;
    }

    public void log(LoggerLevel level, String msg){
        setCurrentLevel(level);
        log(msg);
    }
    public void log(LoggerLevel level,String msg,LogDetail detail){
        setCurrentLogDetail(detail);
        setCurrentLevel(level);
        log(msg);
    }
    public void log(LoggerLevel level,String msg,LogDetail detail,Object ...cx){
        setCurrentLogDetail(detail);
        setCurrentLevel(level);
        log(msg,cx);
    }
    @Override
    public void log(String msg) {
        StringBuilder threadBuilder = new StringBuilder();
        for (int i = 0; i < processor().CHANNELS.size(); i++) {
            if (i==processor().CHANNELS.size()-1){
                threadBuilder.append(processor().CHANNELS.get(i).getCurrentThread().getName());
            }else{
            threadBuilder.append(processor().CHANNELS.get(i).getCurrentThread().getName()).append("-");
            }
        }
        thread = threadBuilder.toString();
        StringBuilder builder = new StringBuilder();
        builder.append("level:").append(currentLevel.getFormat());
        builder.append(",");
        builder.append("thread:").append(thread);
        builder.append(",");
        builder.append("class:").append(ID().name());
        if (msg.contains("\n")){
            if (currentLogDetail!=null){
                StringBuilder details = new StringBuilder();
                currentLogDetail.detailMap.forEach(new BiConsumer<String, Object>() {
                    @Override
                    public void accept(String s, Object o) {
                        details.append(s).append(":").append(o).append("\n");
                    }
                });
                msg = ColorUtils.color256(currentLevel.getRgb256(),"{ ") +"\n"+msg+"\ndetails:\n"+details.toString()+ColorUtils.color256(currentLevel.getRgb256(),"\n}");
            }else{
            msg = ColorUtils.color256(currentLevel.getRgb256(),"{ ") +"\n"+msg+ColorUtils.color256(currentLevel.getRgb256(),"\n}");

            }
        }else{
            if (currentLogDetail!=null){
                StringBuilder details = new StringBuilder();
                details.append(ColorUtils.color256(currentLevel.getRgb256()," {") ).append("\ndetails:\n");
                currentLogDetail.detailMap.forEach(new BiConsumer<>() {
                    @Override
                    public void accept(String s, Object o) {
                        details.append(s).append(":").append(o).append("\n");
                    }
                });
                details.append(ColorUtils.color256(currentLevel.getRgb256(),"}"));
                msg = msg+details;
            }
        }
        if (currentLevel.equals(LoggerLevel.EXCEPTION)||currentLevel.equals(LoggerLevel.ERROR)){
          String finalForm =   format().formatAll(builder.toString())+": "+msg;
          this.processor().pushAll(new LogMessage<>(ColorUtils.color256_BACKGROUNDED(currentLevel.getRgb256(), StringUtils.removeColorCodes(finalForm)),currentLogPriority));
        }else{
        this.processor().pushAll(new LogMessage<>(format().formatAll(builder.toString())+": "+msg,currentLogPriority));
        }
    }

    @Override
    public void log(String msg, Object... cx) {
        if (msg == null || cx == null) {
            throw new NullPointerException("msg or cx is null!");
        }
        for (int i = 0; i < cx.length; i++) {
            msg = msg.replace("%" + i, cx[i].toString());
        }
        log(currentLevel,msg);
    }

    @Override
    public void log(int msg) {
        log(msg+"");
    }

    @Override
    public void log(double msg) {
        log(msg+"");
    }

    @Override
    public void log(float msg) {
        log(msg+"");
    }

    @Override
    public void log(long msg) {
        log(msg+"");
    }

    @Override
    public void log(boolean msg) {
        log(msg+"");
    }

    public void lifecycle(String msg){
        log(LoggerLevel.LIFECYCLE,msg);
    }
    public void lifecycle(String msg,Object...cx){
        setCurrentLevel(LoggerLevel.LIFECYCLE);
        log(msg,cx);
    }

    public void info(String msg){
        log(LoggerLevel.INFO,msg);
    }
    public void info(String msg,Object...cx){
        setCurrentLevel(LoggerLevel.INFO);
        log(msg,cx);
    }

    public void warning(String msg){
        log(LoggerLevel.WARNING,msg);
    }
    public void warning(String msg,Object...cx){
        setCurrentLevel(LoggerLevel.WARNING);
        log(msg,cx);
    }

    public void error(String msg){
        log(LoggerLevel.ERROR,msg);
    }
    public void error(String msg,Object...cx){
        setCurrentLevel(LoggerLevel.ERROR);
        log(msg,cx);
    }

    public void exception(String msg){
        log(LoggerLevel.EXCEPTION,msg);
    }
    public void exception(String msg,Object...cx){
        setCurrentLevel(LoggerLevel.EXCEPTION);
        log(msg,cx);
    }

    public void trace(String msg){
        LogDetail logDetail = new LogDetail();
        STACK_WALKER.walk(frames -> {
            frames.limit(maxFrames).forEach(frame -> {
                logDetail.detailMap.put("ClassName", frame.getClassName());
                logDetail.detailMap.put("MethodName", frame.getMethodName());
                logDetail.detailMap.put("FileName", frame.getFileName());
                logDetail.detailMap.put("LineNumber", frame.getLineNumber());
                logDetail.detailMap.put("IsNativeMethod", frame.isNativeMethod());
                logDetail.detailMap.put("DeclaringClass", frame.getDeclaringClass().getName());
                logDetail.detailMap.put("trace", frame.toStackTraceElement().toString());
            });
           return 0;
        });
        log(LoggerLevel.TRACE,msg,logDetail);
    }
    public void trace(String msg,Object ...cx){
        LogDetail logDetail = new LogDetail();
        STACK_WALKER.walk(frames -> {
            frames.limit(maxFrames).forEach(frame -> {
                logDetail.detailMap.put("ClassName", frame.getClassName());
                logDetail.detailMap.put("MethodName", frame.getMethodName());
                logDetail.detailMap.put("FileName", frame.getFileName());
                logDetail.detailMap.put("LineNumber", frame.getLineNumber());
                logDetail.detailMap.put("IsNativeMethod", frame.isNativeMethod());
                logDetail.detailMap.put("DeclaringClass", frame.getDeclaringClass().getName());
                logDetail.detailMap.put("trace", frame.toStackTraceElement().toString());
            });
            return 0;
        });
        log(LoggerLevel.TRACE,msg,logDetail,cx);
    }
}
