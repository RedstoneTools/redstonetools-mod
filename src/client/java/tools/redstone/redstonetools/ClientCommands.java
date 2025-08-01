package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.commands.update.UpdateFeature;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.utils.DependencyLookup;

public class ClientCommands {
	public static void registerCommands() {
		if (DependencyLookup.WORLDEDIT_PRESENT) {
			UpdateFeature.registerCommand();
			BinaryBlockReadFeature.registerCommand();
			ColorCodeFeature.registerCommand();
			MinSelectionFeature.registerCommand();
			RStackFeature.registerCommand();
		}

		ColoredFeature.registerCommand();
		CopyStateFeature.registerCommand();
		BaseConvertFeature.registerCommand();
		EditMacroFeature.registerCommand();
		MacroFeature.registerCommand();
		QuickTpFeature.registerCommand();
		SignalStrengthBlockFeature.registerCommand();
		GiveMeFeature.registerCommand();
		AirPlaceFeature.registerCommand();
		BigDustFeature.registerCommand();
		AutoDustFeature.registerCommand();
	}
}
