package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.*;

public class ClientCommands {
	public static void registerCommands() {
		BaseConvertFeature.registerCommand();
		EditMacroFeature.registerCommand();
		MacroFeature.registerCommand();
		ReachFeature.registerCommand();
		GiveMeFeature.registerCommand();
		AirPlaceFeature.registerCommand();
		AutoRotateFeature.registerCommand();
		ClickContainerFeature.registerCommand();
		BigDustFeature.registerCommand();
		AutoDustClient.registerHandler();
		AutoRotateClient.registerHandler();
		ClickContainerClient.registerHandler();
	}
}
