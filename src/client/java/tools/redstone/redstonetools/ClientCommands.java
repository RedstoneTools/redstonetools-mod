package tools.redstone.redstonetools;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.utils.DependencyLookup;

public class ClientCommands {
	public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		if (!DependencyLookup.REDSTONE_TOOLS_SERVER_PRESENT) {
			BaseConvertClient.INSTANCE.registerCommand(dispatcher, registryAccess);
			ReachClient.INSTANCE.registerCommand(dispatcher, registryAccess);
			GiveMeClient.INSTANCE.registerCommand(dispatcher, registryAccess);
			QuickTpClient.INSTANCE.registerCommand(dispatcher, registryAccess);
		}
		ClientDataFeature.INSTANCE.registerCommand(dispatcher, registryAccess);
		PrintFeature.INSTANCE.registerCommand(dispatcher, registryAccess);
		EditMacroFeature.INSTANCE.registerCommand(dispatcher, registryAccess);
		MacroFeature.INSTANCE.registerCommand(dispatcher, registryAccess);
		AirPlaceFeature.INSTANCE.registerCommand(dispatcher, registryAccess);
		RstFeature.INSTANCE.registerCommand(dispatcher, registryAccess);
		BigDustFeature.INSTANCE.registerCommand(dispatcher, registryAccess);
	}
}
