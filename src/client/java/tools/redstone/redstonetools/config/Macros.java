package tools.redstone.redstonetools.config;

import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import kr1v.malilibApi.InternalMalilibApi;
import kr1v.malilibApi.ModRepresentation;
import kr1v.malilibApi.annotation.Config;
import kr1v.malilibApi.config._new.ConfigList;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.config.option.ConfigMacro;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

@Config(RedstoneTools.MOD_ID)
public class Macros {
	public static final ConfigList<ConfigMacro> MACROS = new ConfigList<>(
		"Macros",
		MacroManager.getDefaultMacros(),
		() -> {
			var newMacro = new ConfigMacro("macro " + (getMacros().size()), true, false, List.of(), "", KeybindSettings.PRESS_ALLOWEXTRA);
			if (MacroManager.nameExists(newMacro.getName(), newMacro)) {
				newMacro.setMacroName(newMacro.getName() + " " + UUID.randomUUID());
			}
			return newMacro;
		},
		"Edit macros"
	);

	public static List<ConfigMacro> getMacros() {
		//noinspection ConstantValue
		if (MACROS == null) return List.of();
		return MACROS.getList();
	}

	private static ModRepresentation.Tab TAB;

	public static ModRepresentation.Tab getTab() {
		if (TAB == null) TAB = initTab();
		return TAB;
	}

	private static ModRepresentation.Tab initTab() {
		ModRepresentation.Tab macrosTab = null;
		for (ModRepresentation.Tab tab : InternalMalilibApi.getMod(RedstoneTools.MOD_ID).tabs) {
			if (tab.translationKey().equals("Macros")) {
				macrosTab = tab;
				break;
			}
		}
		if (macrosTab == null) throw new IllegalStateException("Macros tab not found");
		return macrosTab;
	}
}
