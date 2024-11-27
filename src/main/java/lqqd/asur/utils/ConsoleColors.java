package lqqd.asur.utils;


public class ConsoleColors {
    // Reset
    public static final String RESET = "\033[0m";

    // Text Colors
    public static final String BLACK = "\033[30m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";
    public static final String ORANGE = "\033[38;5;208m";

    // Background Colors
    public static final String BLACK_BACKGROUND = "\033[40m";
    public static final String RED_BACKGROUND = "\033[41m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String MAGENTA_BACKGROUND = "\033[45m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String WHITE_BACKGROUND = "\033[47m";

    public static final String BOLD = "\033[1m";
    public static final String UNDERLINE = "\033[4m";

    /**
     * Returns a string with the specified color applied.
     *
     * @param text  The text to colorize.
     * @param color The ANSI color code.
     * @return The colorized text.
     */
    public static String colorText(String text, String color) {
        return color + text + RESET;
    }

    public static void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

}

