package tools.redstone.redstonetools.utils;

//? if fabric {
/*import net.fabricmc.loader.api.FabricLoader;
 *///? }

public class DependencyLookup {
	//? if fabric {
	/*public static final boolean WORLDEDIT_PRESENT = FabricLoader.getInstance().isModLoaded("worldedit");
	 *///? } else {
	public static final boolean WORLDEDIT_PRESENT =
		org.bukkit.Bukkit.getPluginManager().getPlugin("WorldEdit") != null
			|| org.bukkit.Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") != null;
	//? }

	public static boolean REDSTONE_TOOLS_SERVER_PRESENT = false;
}
