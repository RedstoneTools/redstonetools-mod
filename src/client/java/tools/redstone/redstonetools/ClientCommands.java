package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.*;

public class ClientCommands {
	public static void registerCommands() {
		BaseConvertFeature.INSTANCE.registerCommand();
		EditMacroFeature.INSTANCE.registerCommand();
		MacroFeature.INSTANCE.registerCommand();
		ReachFeature.INSTANCE.registerCommand();
		GiveMeFeature.INSTANCE.registerCommand();
		AirPlaceFeature.INSTANCE.registerCommand();
		AutoRotateFeature.INSTANCE.registerCommand();
		ClickContainerFeature.INSTANCE.registerCommand();
		RstFeature.INSTANCE.registerCommand();
		BigDustFeature.INSTANCE.registerCommand();
		AutoDustClient.registerHandler();
		AutoRotateClient.registerHandler();
		ClickContainerClient.registerHandler();
	}
}
