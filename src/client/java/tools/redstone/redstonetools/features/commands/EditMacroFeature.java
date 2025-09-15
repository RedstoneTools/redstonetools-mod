package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import tools.redstone.redstonetools.malilib.GuiMacroManager;


public class EditMacroFeature {
	public static final EditMacroFeature INSTANCE = new EditMacroFeature();

	protected EditMacroFeature() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
			dispatcher.register(ClientCommandManager.literal("edit-macros")
				.executes(commandContext -> {
					MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new GuiMacroManager()));
					return 1;
				}));
	}
}
