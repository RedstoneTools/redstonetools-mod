package tools.redstone.redstonetools.features.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.malilib.GuiConfigs;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class RstFeature {
	public static final RstFeature INSTANCE = new RstFeature();

	protected RstFeature() {
	}

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) ->
			dispatcher.register(ClientCommandManager.literal("rst")
				.executes(commandContext -> {
					MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new GuiConfigs()));
					if (GuiConfigs.tab == GuiConfigs.ConfigGuiTab.MACROS)
						GuiConfigs.tab = GuiConfigs.ConfigGuiTab.GENERAL;
					return 1;
				})));
	}
}
