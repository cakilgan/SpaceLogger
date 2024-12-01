package io.github.cakilgan.spacelogger.io;

public class LogMessage<T>{
    LogPriority priority = LogPriority.TRASH;
    T msg;
    public LogMessage(T msg){
        this.msg = msg;
    }
    public LogMessage(T msg,LogPriority priority){
        this.msg = msg;
        this.priority = priority;
    }
    public void setPriority(LogPriority priority) {
        this.priority = priority;
    }
    public LogPriority priority() {
        return priority;
    }
    public void setMsg(T msg) {
        this.msg = msg;
    }
    public T msg() {
        return msg;
    }
}
