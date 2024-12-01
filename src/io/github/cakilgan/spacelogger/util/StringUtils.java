package io.github.cakilgan.spacelogger.util;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern ANSI_COLOR_CODE_PATTERN = Pattern.compile("\u001B\\[[0-9;]*m");
    public static String removeColorCodes(String input) {
        if (input == null) {
            return null;
        }
        return ANSI_COLOR_CODE_PATTERN.matcher(input).replaceAll("");
    }

    public static String get(String from, String cx) {
        String[] splitted = cx.split(",");

        for (String string : splitted) {
            String[] keyValue = string.split(":");

            if (keyValue.length == 2 && keyValue[0].trim().equals(from)) {
                return keyValue[1].trim();
            }
        }
        return null;
    }

    public static String surround(String cx,char s1,char s2){
        return s1+cx+s2;
    }
}
