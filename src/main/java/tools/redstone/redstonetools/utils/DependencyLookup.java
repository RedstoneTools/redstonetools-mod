package tools.redstone.redstonetools.utils;

import net.fabricmc.loader.api.FabricLoader;

public class DependencyLookup {
    public static final boolean WORLDEDIT_PRESENT = FabricLoader.getInstance().isModLoaded("worldedit");
}
