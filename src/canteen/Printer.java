package canteen;

public class Printer {

    // Divider lines
    public static void printLine() {
        System.out.println("=================================================");
    }

    public static void printDoubleLine() {
        System.out.println("=================================================");
    }

    // Success / Error / Info / Warning messages
    public static void success(String msg) {
        System.out.println("  [OK]  " + msg);
    }

    public static void error(String msg) {
        System.out.println("  [ERR] " + msg);
    }

    public static void info(String msg) {
        System.out.println("  [i]   " + msg);
    }

    public static void warning(String msg) {
        System.out.println("  [!]   " + msg);
    }
}
