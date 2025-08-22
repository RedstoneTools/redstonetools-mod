package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.features.toggleable.ClickContainerFeature;
import tools.redstone.redstonetools.utils.DependencyLookup;

public class Commands {
	public static void registerCommands() {
		if (DependencyLookup.WORLDEDIT_PRESENT) {
			BinaryBlockReadFeature.INSTANCE.registerCommand();
			ColorCodeFeature.INSTANCE.registerCommand();
			MinSelectionFeature.INSTANCE.registerCommand();
			RStackFeature.INSTANCE.registerCommand();
		}
		ItemComponentsFeature.registerCommand(); // temp
		ItemBindFeature.INSTANCE.registerCommand();
		QuickTpFeature.INSTANCE.registerCommand();
		SignalStrengthBlockFeature.INSTANCE.registerCommand();
		AutoDustFeature.INSTANCE.registerCommand();
		AutoRotateFeature.INSTANCE.registerCommand();
		ClickContainerFeature.INSTANCE.registerCommand();
		ColoredFeature.INSTANCE.registerCommand();
		CopyStateFeature.INSTANCE.registerCommand();
	}
}
