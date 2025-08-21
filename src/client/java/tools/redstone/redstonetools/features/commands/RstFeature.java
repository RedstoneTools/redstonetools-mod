package tools.redstone.redstonetools.features.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.malilib.GuiConfigs;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class RstFeature {
	public static void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("rst")
			.executes(commandContext -> {
				MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new GuiConfigs()));
				GuiConfigs.tab = GuiConfigs.ConfigGuiTab.GENERAL;
				return 1;
			})));
	}
}
