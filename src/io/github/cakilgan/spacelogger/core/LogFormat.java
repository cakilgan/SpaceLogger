package io.github.cakilgan.spacelogger.core;

import java.util.List;

public abstract class LogFormat {
    public static abstract class LogElement{
        protected String form;
        public LogElement(String defaultForm){
            form = defaultForm;
        }
        public abstract void format(String cx);
    }
    protected List<LogElement> ELEMENTS;
    public abstract String formatAll(String cx);
}
