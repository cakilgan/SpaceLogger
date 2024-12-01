package io.github.cakilgan.spacelogger.core;

public interface ILogger {
    void log(String msg);
    void log(String msg,Object ...cx);
    void log(int msg);
    void log(double msg);
    void log(float msg);
    void log(long msg);
    void log(boolean msg);
}
