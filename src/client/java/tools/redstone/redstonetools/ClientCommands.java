package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.BaseConvertFeature;
import tools.redstone.redstonetools.features.commands.EditMacroFeature;
import tools.redstone.redstonetools.features.commands.MacroFeature;
import tools.redstone.redstonetools.features.commands.ReachFeature;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustClient;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;

public class ClientCommands {
	public static void registerCommands() {
		BaseConvertFeature.registerCommand();
		EditMacroFeature.registerCommand();
		MacroFeature.registerCommand();
		ReachFeature.registerCommand();
		AirPlaceFeature.registerCommand();
		BigDustFeature.registerCommand();
		AutoDustClient.registerHandler();
	}
}
