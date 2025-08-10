package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;

public class ClientCommands {
	public static void registerCommands() {
		BaseConvertFeature.registerCommand();
		EditMacroFeature.registerCommand();
		MacroFeature.registerCommand();
		AirPlaceFeature.registerCommand();
	}
}
