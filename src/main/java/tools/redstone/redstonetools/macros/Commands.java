package tools.redstone.redstonetools.macros;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.commands.update.UpdateFeature;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.utils.DependencyLookup;

public class Commands {
	public static void registerCommands() {
//		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("edit-macros")
//				.executes(commandContext -> {
//					MinecraftClient.getInstance().setScreen(new MacrosScreen(MinecraftClient.getInstance().currentScreen, MacroManager.getMacros()));
//					return 1;
//				})));
		// for some reason setScreen doesn't work in commands. sucks.

		if (DependencyLookup.WORLDEDIT_PRESENT) {
			UpdateFeature.registerCommand();
			BinaryBlockReadFeature.registerCommand();
			ColorCodeFeature.registerCommand();
			MinSelectionFeature.registerCommand();
			RStackFeature.registerCommand();
		}

		BaseConvertFeature.registerCommand();
		ColoredFeature.registerCommand();
		CopyStateFeature.registerCommand();
		ItemBindFeature.registerCommand();
		MacroFeature.registerCommand();
		QuickTpFeature.registerCommand();
		SignalStrengthBlockFeature.registerCommand();
		GiveMeFeature.registerCommand();
		AirPlaceFeature.registerCommand();
		BigDustFeature.registerCommand();
		AutoDustFeature.registerCommand();
	}
}
