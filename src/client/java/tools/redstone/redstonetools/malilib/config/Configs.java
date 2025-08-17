package tools.redstone.redstonetools.malilib.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.RedstoneTools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configs implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = RedstoneTools.MOD_ID + ".json";

	public static class General {
		public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("hotkeyOpenMenu", "V,C", "Hotkey to open this menu");
		public static final ConfigBoolean BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS = new ConfigBoolean("improvedCommandSuggestions", true,
				"""
						Enables/disables improved suggestions when typing commands.
						
						When typing "/give @s redstblock" in chat, with this disabled it will give no suggestions (default behaviour, or "prefix matching"), but with
						this enabled it will give "redstone_block" as a suggestion (so called "fuzzy matching").""");
		public static final ConfigBoolean PRESET_AIRPLACE_SHOW_OUTLINE =        new ConfigBoolean("default/airplaceShowOutline", true);
		public static final ConfigInteger PRESET_BIGDUST_HEIGHT_IN_PIXELS =     new ConfigInteger("default/bigdustHeightInPixels", 3);
		public static final ConfigInteger PRESET_SSB_SIGNAL_STRENGTH =          new ConfigInteger("default/ssbSignalStrength", 15);
		public static final ConfigDouble  PRESET_QUICKTP_DISTANCE =             new ConfigDouble ("default/quicktpDistance", 50.0);
		public static final ConfigBoolean PRESET_QUICKTP_THROUGH_FLUIDS =       new ConfigBoolean("default/quicktpThroughFluids", false);
		public static final ConfigBoolean PRESET_QUICKTP_RESET_VELOCITY =       new ConfigBoolean("default/quicktpResetVelocity", true);
		public static final ConfigInteger PRESET_GIVEME_COUNT =                 new ConfigInteger("default/gCount", 1, 1, 64);
		public static final ConfigInteger PRESET_BINARYBLOCKREAD_OFFSET =       new ConfigInteger("default//readOffset", 2);
		public static final ConfigString  PRESET_BINARYBLOCKREAD_ONBLOCK =      new ConfigString ("default//readOnBlock", "redstone_lamp[lit=true]");
		public static final ConfigInteger PRESET_BINARYBLOCKREAD_TOBASE =       new ConfigInteger("default//readToBase", 10);
		public static final ConfigBoolean PRESET_BINARYBLOCKREAD_REVERSEBITS =  new ConfigBoolean("default//readReverseBits", false);
//		public static final ConfigOptionList PRESET_COLORED_BLOCKTYPE = new ConfigOptionList("default //rstack direction value", );
//		public static final ConfigOptionList PRESET_COLORCODE_ONLYCOLOR = new ConfigButtonOptionList("default //rstack direction value", );
//		public static final ConfigOptionList PRESET_SSB_SIGNAL_BLOCK = new ConfigButtonOptionList("default //rstack direction value", );
		public static final ConfigInteger PRESET_RSTACK_COUNT =                 new ConfigInteger("default//rstackCount", 1);
//		public static final ConfigOptionList PRESET_RSTACK_DIRECTION = new ConfigButtonOptionList("default //rstack direction value", );
		public static final ConfigInteger PRESET_RSTACK_OFFSET =                new ConfigInteger("default//rstackOffset", 2);

		public static final Map<String, IConfigBase> CONFIG = new HashMap<>();

		public static final List<? extends IConfigBase> OPTIONS = List.of(
				HOTKEY_OPEN_GUI,
				BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS,
				PRESET_AIRPLACE_SHOW_OUTLINE,
				PRESET_BIGDUST_HEIGHT_IN_PIXELS,
				PRESET_SSB_SIGNAL_STRENGTH,
				PRESET_QUICKTP_DISTANCE,
				PRESET_QUICKTP_THROUGH_FLUIDS,
				PRESET_QUICKTP_RESET_VELOCITY,
				PRESET_GIVEME_COUNT,
				PRESET_BINARYBLOCKREAD_OFFSET,
				PRESET_BINARYBLOCKREAD_ONBLOCK,
				PRESET_BINARYBLOCKREAD_TOBASE,
				PRESET_BINARYBLOCKREAD_REVERSEBITS,
				PRESET_RSTACK_COUNT,
				PRESET_RSTACK_OFFSET
		);

		public static class Callback<T extends IConfigBase> implements IValueChangeCallback<T> {
			@Override
			public void onValueChanged(T iConfigBase) {
			}
		}

		static {
			CONFIG.put("PRESET_AIRPLACE_SHOW_OUTLINE", PRESET_AIRPLACE_SHOW_OUTLINE);
			CONFIG.put("PRESET_BIGDUST_HEIGHT_IN_PIXELS", PRESET_BIGDUST_HEIGHT_IN_PIXELS);
			CONFIG.put("PRESET_SSB_SIGNAL_STRENGTH", PRESET_SSB_SIGNAL_STRENGTH);

			CONFIG.put("PRESET_QUICKTP_DISTANCE", PRESET_QUICKTP_DISTANCE);
			CONFIG.put("PRESET_QUICKTP_THROUGH_FLUIDS", PRESET_QUICKTP_THROUGH_FLUIDS);
			CONFIG.put("PRESET_QUICKTP_RESET_VELOCITY", PRESET_QUICKTP_RESET_VELOCITY);

			CONFIG.put("PRESET_GIVEME_COUNT", PRESET_GIVEME_COUNT);

			CONFIG.put("PRESET_BINARYBLOCKREAD_OFFSET", PRESET_BINARYBLOCKREAD_OFFSET);
			CONFIG.put("PRESET_BINARYBLOCKREAD_ONBLOCK", PRESET_BINARYBLOCKREAD_ONBLOCK);
			CONFIG.put("PRESET_BINARYBLOCKREAD_TOBASE", PRESET_BINARYBLOCKREAD_TOBASE);
			CONFIG.put("PRESET_BINARYBLOCKREAD_REVERSEBITS", PRESET_BINARYBLOCKREAD_REVERSEBITS);

			CONFIG.put("PRESET_RSTACK_COUNT", PRESET_RSTACK_COUNT);
			CONFIG.put("PRESET_RSTACK_OFFSET", PRESET_RSTACK_OFFSET);

			PRESET_AIRPLACE_SHOW_OUTLINE.setValueChangeCallback(new Callback<>());
			PRESET_BIGDUST_HEIGHT_IN_PIXELS.setValueChangeCallback(new Callback<>());
			PRESET_SSB_SIGNAL_STRENGTH.setValueChangeCallback(new Callback<>());

			PRESET_QUICKTP_DISTANCE.setValueChangeCallback(new Callback<>());
			PRESET_QUICKTP_THROUGH_FLUIDS.setValueChangeCallback(new Callback<>());
			PRESET_QUICKTP_RESET_VELOCITY.setValueChangeCallback(new Callback<>());

			PRESET_GIVEME_COUNT.setValueChangeCallback(new Callback<>());

			PRESET_BINARYBLOCKREAD_OFFSET.setValueChangeCallback(new Callback<>());
			PRESET_BINARYBLOCKREAD_ONBLOCK.setValueChangeCallback(new Callback<>());
			PRESET_BINARYBLOCKREAD_TOBASE.setValueChangeCallback(new Callback<>());
			PRESET_BINARYBLOCKREAD_REVERSEBITS.setValueChangeCallback(new Callback<>());

			PRESET_RSTACK_COUNT.setValueChangeCallback(new Callback<>());
			PRESET_RSTACK_OFFSET.setValueChangeCallback(new Callback<>());
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
