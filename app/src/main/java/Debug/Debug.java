package Debug;

public class Debug {


    private static void print(String flag, String msg) {
        System.out.println("[" + flag + "]: " + msg);
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

        System.out.println("ERROR " + msg);
    }

    public static void toggleDebugInfo() {
        printDebugInfo = !printDebugInfo;
        Debug.print("SYSTEM", "debug info will be " + (printDebugInfo ? "displayed" : "hidden"));
    }
    public static void toggleWarnings() {
        printWarnings = !printErrors;
        Debug.print("SYSTEM", "warnings will be " + (printWarnings ? "displayed" : "hidden"));
    }
    public static void toggleErrors() {
        printErrors = !printErrors;
        Debug.print("SYSTEM", "error messages will be " + (printErrors ? "displayed" : "hidden"));
    }
}
