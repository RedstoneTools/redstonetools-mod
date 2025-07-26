package tools.redstone.redstonetools.macros;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.commands.update.UpdateFeature;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.macros.gui.malilib.MacrosScreen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.*;

public class Commands {
	public static void registerCommands() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("edit-macros")
				.executes(commandContext -> {
					MinecraftClient.getInstance().setScreen(new MacrosScreen(MinecraftClient.getInstance().currentScreen, MacroManager.getMacros()));
					return 1;
				})));

		BaseConvertFeature.registerCommand();
		UpdateFeature.registerCommand();
		ColorCodeFeature.registerCommand();
		ColoredFeature.registerCommand();
		CopyStateFeature.registerCommand();
		ItemBindFeature.registerCommand();
		MacroFeature.registerCommand();
		MinSelectionFeature.registerCommand();
		QuickTpFeature.registerCommand();
		RStackFeature.registerCommand();
		SignalStrengthBlockFeature.registerCommand();

		AirPlaceFeature.registerCommand();
		BigDustFeature.registerCommand();
		AutoDustFeature.registerCommand();
	}
}
