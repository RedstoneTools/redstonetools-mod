package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.*;

public class ClientCommands {
	public static void registerCommands() {
		BaseConvertClient.INSTANCE.registerCommand();
		ClientDataFeature.INSTANCE.registerCommand();
		PrintFeature.INSTANCE.registerCommand();
		EditMacroFeature.INSTANCE.registerCommand();
		MacroFeature.INSTANCE.registerCommand();
		ReachClient.INSTANCE.registerCommand();
		GiveMeClient.INSTANCE.registerCommand();
		AirPlaceFeature.INSTANCE.registerCommand();
		AutoRotateFeature.INSTANCE.registerCommand();
		ClickContainerFeature.INSTANCE.registerCommand();
		RstFeature.INSTANCE.registerCommand();
		BigDustFeature.INSTANCE.registerCommand();
		QuickTpClient.INSTANCE.registerCommand();
	}
}
