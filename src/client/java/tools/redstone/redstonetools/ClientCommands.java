package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustClient;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;

public class ClientCommands {
	public static void registerCommands() {
		BaseConvertFeature.registerCommand();
		EditMacroFeature.registerCommand();
		MacroFeature.registerCommand();
		AirPlaceFeature.registerCommand();
		BigDustFeature.registerCommand();
		AutoDustClient.registerHandler();
	}
}
