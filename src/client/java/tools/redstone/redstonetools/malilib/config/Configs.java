package tools.redstone.redstonetools.malilib.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.malilib.widget.ConfigMacro;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = RedstoneTools.MOD_ID + ".json";

	public static class General {
		public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("hotkeyOpenMenu", "V,C");
		public static final List<ConfigHotkey> OPTIONS = List.of(HOTKEY_OPEN_GUI);
	}

	public static class Macros {

		public static final ConfigMacro HOTKEY = new ConfigMacro("test",
				true,
				true,
				"",
				KeybindSettings.DEFAULT,
				"comment",
				"Test",
				"tesT",
				ImmutableList.of("/time set 0"),
				List.of());
		public static final List<ConfigMacro> MACROS = List.of(HOTKEY);


	}

	public static void loadFromFile() {
		File configFile = new File(MinecraftClient.getInstance().runDirectory, "config/" + CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", General.OPTIONS);
				ConfigUtils.readConfigBase(root, "Hotkeys", Macros.MACROS);
			}
		}
	}

	public static void saveToFile() {
		File dir = new File(MinecraftClient.getInstance().runDirectory, "config");

		if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", General.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Hotkeys", Macros.MACROS);

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
