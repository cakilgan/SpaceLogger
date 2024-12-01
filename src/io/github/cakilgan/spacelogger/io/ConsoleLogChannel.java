package io.github.cakilgan.spacelogger.io;

public class ConsoleLogChannel extends LogChannel{
    public ConsoleLogChannel() {
        super("defaultSOUT");
    }
    @Override
    protected void processMsg(LogMessage<?> logMsg) {
        System.out.println(logMsg.msg());
    }
}
