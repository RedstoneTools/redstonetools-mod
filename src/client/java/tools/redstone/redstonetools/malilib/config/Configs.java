package tools.redstone.redstonetools.malilib.config;

import com.google.common.collect.ImmutableList;
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
import java.util.ArrayList;
import java.util.List;

public class Configs implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = RedstoneTools.MOD_ID + ".json";

	public static class Toggles {
		public static final ConfigBooleanHotkeyed AIRPLACE = new ConfigBooleanHotkeyed("Airplace", false, "", "Whether or not airplace should be enabled");
		public static final ConfigBooleanHotkeyed AUTODUST = new ConfigBooleanHotkeyed("Autodust", false, "", "Whether or not autodust should be enabled");
		public static final ConfigBooleanHotkeyed AUTOROTATE = new ConfigBooleanHotkeyed("Autorotate", false, "", "Whether or not autorotate should be enabled");
		public static final ConfigBooleanHotkeyed BIGDUST = new ConfigBooleanHotkeyed("Bigdust", false, "", "Whether or not bigdust should be enabled");
		public static final ConfigBooleanHotkeyed CLICKCONTAINERS = new ConfigBooleanHotkeyed("Clickcontainers", false, "", "Whether or not clickcontainer should be enabled");

		public static final List<? extends IConfigBase> TOGGLES = List.of(
			AIRPLACE,
			AUTODUST,
			AUTOROTATE,
			BIGDUST,
			CLICKCONTAINERS
		);

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

	public static class ClientData {
		public static final ConfigString VARIABLE_BEGIN_STRING = new ConfigString("Variable begin string", "'", "The string that should be used to denote the start of a variable. Can be empty");
		public static final ConfigString VARIABLE_END_STRING = new ConfigString("Variable end string", "'", "The string that should be used to denote the end of a variable. Can be empty");
		public static final ConfigString MATH_BEGIN_STRING = new ConfigString("Math begin string", "{", "The string that should be used to denote the start of a math expression. Can be empty, unsure if you'd want that though.");
		public static final ConfigString MATH_END_STRING = new ConfigString("Math end string", "}", "The string that should be used to denote the end of a math expression. Can be empty, unsure if you'd want that though.");
		public static final ConfigStringList AUTORUN_FIRST_WORLD_ENTRY = new ConfigStringList("First world entry", ImmutableList.of(), "Commands that will be run the first time you join a world in this session. Ignores entries not starting with a /");
		public static final ConfigStringList AUTORUN_WORLD_ENTRY = new ConfigStringList("World entry", ImmutableList.of(), "Commands that will be run when you join a world. Ignores entries not starting with a /");
		public static final ConfigStringList AUTORUN_DIMENSION_CHANGE = new ConfigStringList("Dimension change", ImmutableList.of(), "Commands that will be run after you change dimensions. Ignores entries not starting with a /");

		public static final List<IConfigBase> OPTIONS = new ArrayList<>();

		static {
			OPTIONS.add(VARIABLE_BEGIN_STRING);
			OPTIONS.add(VARIABLE_END_STRING);
			OPTIONS.add(MATH_BEGIN_STRING);
			OPTIONS.add(MATH_END_STRING);
			OPTIONS.add(AUTORUN_FIRST_WORLD_ENTRY);
			OPTIONS.add(AUTORUN_WORLD_ENTRY);
			OPTIONS.add(AUTORUN_DIMENSION_CHANGE);
		}
	}

	public static class General {
		public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("Hotkey to open menu", "V,C", "Hotkey to open menu");
		public static final ConfigBoolean BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS = new ConfigBoolean("Improved command suggestions", true,
			"""
				Enables/disables improved suggestions when typing commands.
				
				When typing "/give @s redstblock" in chat, with this disabled it will give no suggestions (default behaviour, or "prefix matching"), but with
				this enabled it will give "redstone_block" as a suggestion (so called "fuzzy matching").""");
		public static final ConfigBoolean AIRPLACE_SHOW_OUTLINE = new ConfigBoolean("Airplace showOutline", true, "If enabled, will show a block outline for the block your holding");
		public static final ConfigInteger BIGDUST_HEIGHT_IN_PIXELS = new ConfigInteger("Bigdust heightInPixels", 3, 0, 16, "How tall the redstone hitbox should be");
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
	}

	public static void loadFromFile() {
		File configFile = new File(MinecraftClient.getInstance().runDirectory, "config/" + CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", General.OPTIONS);
				ConfigUtils.readConfigBase(root, "Toggles", Toggles.TOGGLES);
				ConfigUtils.readConfigBase(root, "ClientData", ClientData.OPTIONS);
			}
		}
	}

	public static void saveToFile() {
		File dir = new File(MinecraftClient.getInstance().runDirectory, "config");

		if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", General.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Toggles", Toggles.TOGGLES);
			ConfigUtils.writeConfigBase(root, "ClientData", ClientData.OPTIONS);

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
