package tools.redstone.redstonetools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyLookup {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DependencyLookup.class);
    public static final boolean WORLDEDIT_PRESENT =
            require("com.sk89q.worldedit.WorldEdit");

    private static boolean require(String... classNames) {
        for (String className : classNames) {
            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                return false;
            } catch (Throwable t) {
                LOGGER.warn("Unexpected error while checking for dependency {}", className, t);
                return false;
            }
        }
        return true;
    }
}
