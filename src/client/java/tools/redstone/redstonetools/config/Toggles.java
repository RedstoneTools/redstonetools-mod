package tools.redstone.redstonetools.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import kr1v.malilibApi.annotation.Config;
import tools.redstone.redstonetools.RedstoneTools;

@Config(RedstoneTools.MOD_ID)
public class Toggles {
	public static final ConfigBooleanHotkeyed AIRPLACE = new ConfigBooleanHotkeyed("Airplace", false, "", "Whether or not airplace should be enabled");
	public static final ConfigBooleanHotkeyed AUTODUST = new ConfigBooleanHotkeyed("Autodust", false, "", "Whether or not autodust should be enabled");
	public static final ConfigBooleanHotkeyed AUTOROTATE = new ConfigBooleanHotkeyed("Autorotate", false, "", "Whether or not autorotate should be enabled");
	public static final ConfigBooleanHotkeyed BIGDUST = new ConfigBooleanHotkeyed("Bigdust", false, "", "Whether or not bigdust should be enabled");
	public static final ConfigBooleanHotkeyed CLICKCONTAINERS = new ConfigBooleanHotkeyed("Clickcontainers", false, "", "Whether or not clickcontainer should be enabled");

	static {
		AIRPLACE.getKeybind().setCallback((t, g) -> {
			AIRPLACE.setBooleanValue(!AIRPLACE.getBooleanValue());
			return true;
		});
		AUTODUST.getKeybind().setCallback((t, g) -> {
			AUTODUST.setBooleanValue(!AUTODUST.getBooleanValue());
			return true;
		});
		AUTOROTATE.getKeybind().setCallback((t, g) -> {
			AUTOROTATE.setBooleanValue(!AUTOROTATE.getBooleanValue());
			return true;
		});
		BIGDUST.getKeybind().setCallback((t, g) -> {
			BIGDUST.setBooleanValue(!BIGDUST.getBooleanValue());
			return true;
		});
		CLICKCONTAINERS.getKeybind().setCallback((t, g) -> {
			CLICKCONTAINERS.setBooleanValue(!CLICKCONTAINERS.getBooleanValue());
			return true;
		});
	}
}
