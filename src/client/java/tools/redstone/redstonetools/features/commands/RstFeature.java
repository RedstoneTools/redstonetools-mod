package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import tools.redstone.redstonetools.malilib.GuiConfigs;


public class RstFeature {
	public static final RstFeature INSTANCE = new RstFeature();

	protected RstFeature() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
			dispatcher.register(ClientCommandManager.literal("rst")
				.executes(commandContext -> {
					MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new GuiConfigs()));
					if (GuiConfigs.tab == GuiConfigs.ConfigGuiTab.MACROS)
						GuiConfigs.tab = GuiConfigs.ConfigGuiTab.GENERAL;
					return 1;
				}));
	}
}
