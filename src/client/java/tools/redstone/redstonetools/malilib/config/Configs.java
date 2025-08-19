package tools.redstone.redstonetools.malilib.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.RedstoneTools;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = RedstoneTools.MOD_ID + ".json";

	public static class General {
		public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("Hotkey to open menu", "V,C", "Hotkey to open menu");
		public static final ConfigBoolean BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS = new ConfigBoolean("Improved command suggestions", true,
				"""
						Enables/disables improved suggestions when typing commands.
						
						When typing "/give @s redstblock" in chat, with this disabled it will give no suggestions (default behaviour, or "prefix matching"), but with
						this enabled it will give "redstone_block" as a suggestion (so called "fuzzy matching").""");
		public static final ConfigBooleanHotkeyed AIRPLACE_SHOW_OUTLINE = new ConfigBooleanHotkeyed("/airplace showOutline", true, "", "If enabled, will show a block outline for the block your holding");
		public static final ConfigInteger BIGDUST_HEIGHT_IN_PIXELS = new ConfigInteger("/bigdust heightInPixels", 3, 0, 16, "How tall the redstone hitbox should be");
		public static final List<? extends IConfigBase> OPTIONS = List.of(
				HOTKEY_OPEN_GUI,
				BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS,
				AIRPLACE_SHOW_OUTLINE,
				BIGDUST_HEIGHT_IN_PIXELS
		);

		public static int getHeightInPixels() {
			try {
				return BIGDUST_HEIGHT_IN_PIXELS.getIntegerValue();
			} catch (Exception ignored) {
				return 1;
			}
		}

		static {
			AIRPLACE_SHOW_OUTLINE.getKeybind().setCallback((t, g) -> {
				AIRPLACE_SHOW_OUTLINE.setBooleanValue(!AIRPLACE_SHOW_OUTLINE.getBooleanValue());
				return true;
			});
		}
	}

	public static void loadFromFile() {
		File configFile = new File(MinecraftClient.getInstance().runDirectory, "config/" + CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", General.OPTIONS);
			}
		}
	}

	public static void saveToFile() {
		File dir = new File(MinecraftClient.getInstance().runDirectory, "config");

		if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", General.OPTIONS);

			JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
		}
	}

	@Override
	public void load() {
		loadFromFile();
	}

	@Override
	public void save() {
		saveToFile();
	}
}
