package tools.redstone.redstonetools.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import kr1v.malilibApi.MalilibApi;
import kr1v.malilibApi.annotation.Config;
import tools.redstone.redstonetools.RedstoneTools;

@Config(value = RedstoneTools.MOD_ID, order = 0)
public class General {
	public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("Hotkey to open menu", "V,C", "Hotkey to open menu");
	public static final ConfigBoolean BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS = new ConfigBoolean("Improved command suggestions", true,
		"""
			Enables/disables improved suggestions when typing commands.
			
			When typing "/give @s redstblock" in chat, with this disabled it will give no suggestions (default behaviour, or "prefix matching"), but with
			this enabled it will give "redstone_block" as a suggestion (so called "fuzzy matching").""");
	public static final ConfigBoolean AIRPLACE_SHOW_OUTLINE = new ConfigBoolean("Airplace showOutline", true, "If enabled, will show a block outline for the block your holding");
	public static final ConfigInteger BIGDUST_HEIGHT_IN_PIXELS = new ConfigInteger("Bigdust heightInPixels", 3, 0, 16, "How tall the redstone hitbox should be");

	static {
		HOTKEY_OPEN_GUI.getKeybind().setCallback((action, key) -> {
			MalilibApi.openScreenFor(RedstoneTools.MOD_ID);
			return true;
		});
	}
}
