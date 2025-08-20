package tools.redstone.redstonetools.features.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.malilib.GuiMacroManager;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class EditMacroFeature {
	public static void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("edit-macros")
				.executes(commandContext -> {
					MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new GuiMacroManager()));
					return 1;
				})));
	}
}
