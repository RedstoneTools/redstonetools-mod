package tools.redstone.redstonetools.utils;

public class DebugUtils {

    public static void trace(String name) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        System.out.println("TRACE: " + name);
        int l = stackTraceElements.length;
        for (int i = 2; i < l; i++) {
            System.out.println(" at " + stackTraceElements[i]);
        }
    }

}
