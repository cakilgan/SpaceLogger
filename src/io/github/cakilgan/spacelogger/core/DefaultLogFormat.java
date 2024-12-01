package io.github.cakilgan.spacelogger.core;

import io.github.cakilgan.spacelogger.util.ColorUtils;
import io.github.cakilgan.spacelogger.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DefaultLogFormat extends LogFormat{
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd hh:mm.ss..SS");
    int flagDyeAll = -1;
    public DefaultLogFormat(){
        ELEMENTS = new ArrayList<>();
        ELEMENTS.add(new LogElement("level") {
            @Override
            public void format(String cx) {
                String level = StringUtils.get("level",cx);
                if (level!=null) {
                    LoggerLevel rLevel = LoggerLevel.parse(level);
                    if (rLevel.equals(LoggerLevel.ERROR) || rLevel.equals(LoggerLevel.EXCEPTION)) {
                        flagDyeAll = rLevel.rgb256;
                    }
                    form = ColorUtils.color256_BACKGROUNDED(rLevel.rgb256, StringUtils.surround(rLevel.format, '[', ']')) + "--";
                }else{
                    form = "";
                }
            }
        });
       ELEMENTS.add(new LogElement("date") {
           @Override
           public void format(String cx) {
           form = ColorUtils.color256(ColorUtils.BRIGHT_BLUE,StringUtils.surround(DEFAULT_DATE_FORMAT.format(new Date()),'{','}'))+"--";
           }
       });
       ELEMENTS.add(new LogElement("thread") {
           @Override
           public void format(String cx) {
               String threadName = StringUtils.get("thread",cx);
               if (threadName!=null){
               form = ColorUtils.color256(43,StringUtils.surround(threadName,'[',']'))+"--";
               }else{
                   form = "";
               }
           }
       });
       ELEMENTS.add(new LogElement("class") {
           @Override
           public void format(String cx) {
               String className = StringUtils.get("class",cx);
               if (className!=null){
               form = ColorUtils.color256(123,StringUtils.surround(className,'(',')'));
               }else{
                   form = "";
               }
           }
       });
    }
    @Override
    public String formatAll(String cx) {
        StringBuilder builder = new StringBuilder();
        for (LogElement element : ELEMENTS) {
            element.format(cx);
            builder.append(element.form);
        }
        if (flagDyeAll != -1) {
            String finalForm = builder.toString();
            finalForm = StringUtils.removeColorCodes(finalForm);
            finalForm = ColorUtils.color256_BACKGROUNDED(flagDyeAll, finalForm);
            flagDyeAll = -1;
            return finalForm;
        }
        return builder.toString();
    }
}
