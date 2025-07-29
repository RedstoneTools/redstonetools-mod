package tools.redstone.redstonetools.macros;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.commands.update.UpdateFeature;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.macros.gui.malilib.MacrosScreen;
import tools.redstone.redstonetools.utils.DependencyLookup;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class ClientCommands {
	public static void registerCommands() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("edit-macros")
				.executes(commandContext -> {
					MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new MacrosScreen(MinecraftClient.getInstance().currentScreen, MacroManager.getMacros())));
					return 1;
				})));

		if (DependencyLookup.WORLDEDIT_PRESENT) {
			UpdateFeature.registerCommand();
			BinaryBlockReadFeature.registerCommand();
			ColorCodeFeature.registerCommand();
			MinSelectionFeature.registerCommand();
			RStackFeature.registerCommand();
		}

		ColoredFeature.registerCommand();
		CopyStateFeature.registerCommand();
		ItemBindFeature.registerCommand();
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
