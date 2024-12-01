package io.github.cakilgan.spacelogger.util;

public class ColorUtils {
    public static final int BLACK = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int YELLOW = 3;
    public static final int BLUE = 4;
    public static final int MAGENTA = 5;
    public static final int CYAN = 6;
    public static final int WHITE = 7;
    public static final int BRIGHT_BLACK = 8;
    public static final int BRIGHT_RED = 9;
    public static final int BRIGHT_GREEN = 10;
    public static final int BRIGHT_YELLOW = 11;
    public static final int BRIGHT_BLUE = 12;
    public static final int BRIGHT_MAGENTA = 13;
    public static final int BRIGHT_CYAN = 14;
    public static final int BRIGHT_WHITE = 15;
    public static final String RESET = "\u001B[0m";
    public static String color256(int colorCode, String text) {
        if (colorCode < 0 || colorCode > 255) {
            throw new IllegalArgumentException("Color code must be between 0 and 255");
        }
        return "\u001B[38;5;" + colorCode + "m" + text + RESET;
    }
    public static String color256AsText(int colorCode){
        return "\u001B[38;5;" + colorCode + "m";
    }
    public static String color256AsText_BACKGROUNDED(int colorCode){
        return "\u001B[48;5;" + colorCode + "m";
    }
    public static String color256_BACKGROUNDED(int colorCode, String text) {
        if (colorCode < 0 || colorCode > 255) {
            throw new IllegalArgumentException("Color code must be between 0 and 255");
        }
        return "\033[48;5;"+colorCode+"m" + text + RESET;
    }
    public static String colorize(String text,String colorCode){
        return colorCode+text+RESET;
    }
}
