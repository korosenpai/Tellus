package Debug;

public class Debug {


    private static void print(String flag, String msg) {
        System.out.println("[" + flag + "]: " + msg);
    }

    // do not disactivate
    public static void system(String msg) {
        Debug.print("SYSTEM", msg);
    }

    private static boolean printDebugInfo = true;
    public static void debug(String msg) {
        if (!printDebugInfo) return;

        Debug.print("DEBUG", msg);
    }

    private static boolean printWarnings = true;
    public static void warn(String msg) {
        if (!printWarnings) return;

        Debug.print("WARN", msg);

    }

    private static boolean printErrors = true;
    public static void error(String msg) {
        if (!printErrors) return;

        Debug.print("ERROR", msg);
    }

    public static void toggleDebugInfo() {
        printDebugInfo = !printDebugInfo;
        Debug.system("debug info will be " + (printDebugInfo ? "displayed" : "hidden"));
    }
    public static void toggleWarnings() {
        printWarnings = !printErrors;
        Debug.system("warnings will be " + (printWarnings ? "displayed" : "hidden"));
    }
    public static void toggleErrors() {
        printErrors = !printErrors;
        Debug.system("error messages will be " + (printErrors ? "displayed" : "hidden"));
    }
}
