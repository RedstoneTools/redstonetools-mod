package tools.redstone.redstonetools.config;

import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import tools.redstone.redstonetools.config.option.ConfigMacro;
import tools.redstone.redstonetools.macros.actions.CommandAction;

import java.util.ArrayList;
import java.util.List;

public class MacroManager {
	public static boolean shouldMute;

	public static ConfigMacro getMacro(String name) {
		for (ConfigMacro macro : Macros.getMacros()) {
			if (macro.getMacroName().equals(name)) {
				return macro;
			}
		}

		return null;
	}

	public static boolean nameExists(String name, ConfigMacro exclude) {
		for (ConfigMacro macro : Macros.getMacros()) {
			if (macro == exclude) continue;
			if (macro.getMacroName().equals(name)) return true;
		}
		return false;
	}

	public static List<ConfigMacro> getDefaultMacros() {
		return new ArrayList<>(List.of(
			createCommandMacro("redstoner", new String[]{
				"/gamerule doTileDrops false",
				"/gamerule doTraderSpawning false",
				"/gamerule doWeatherCycle false",
				"/gamerule doDaylightCycle false",
				"/gamerule doMobSpawning false",
				//? if <1.21.11
				"/gamerule doContainerDrops false",
				"/time set noon",
				"/weather clear"
			}),
			createCommandMacro("copystate", new String[]{"/copystate" }),
			createCommandMacro("itembind", new String[]{"/itembind" }),
			createCommandMacro("minsel", new String[]{"//minsel" }),
			createCommandMacro("quicktp", new String[]{"/quicktp" }),
			createCommandMacro("binaryblockread", new String[]{"//binaryblockread" }),
			createCommandMacro("rstack", new String[]{"//rstack" }),
			createCommandMacro("update", new String[]{"//update" })
		));
	}

	public static ConfigMacro createCommandMacro(String name, String[] commands) {
		var actions = new CommandAction[commands.length];
		for (int i = 0; i < commands.length; i++) {
			actions[i] = new CommandAction(commands[i]);
		}

		return new ConfigMacro(name, true, false, List.of(actions), "", KeybindSettings.PRESS_ALLOWEXTRA);
	}
}
