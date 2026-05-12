package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import kr1v.malilibApi.InternalMalilibApi;
import kr1v.malilibApi.MalilibApi;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.config.Macros;


public class RstFeature {
	public static final RstFeature INSTANCE = new RstFeature();

	protected RstFeature() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
			dispatcher.register(ClientCommandManager.literal("rst")
				.executes(commandContext -> {
					MinecraftClient.getInstance().send(() -> {
						if (InternalMalilibApi.getMod(RedstoneTools.MOD_ID).activeTab() == Macros.getTab()) {
							InternalMalilibApi.getMod(RedstoneTools.MOD_ID).setActiveTab(null);
						}
						MalilibApi.openScreenFor(RedstoneTools.MOD_ID);
					});
					return 1;
				}));
	}
}
