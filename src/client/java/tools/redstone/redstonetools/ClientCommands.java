package tools.redstone.redstonetools;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
//? if >=1.21.11 {
/*import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;
*///?}
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.utils.DependencyLookup;

import java.util.function.Predicate;

public class ClientCommands {
	public static final Predicate<FabricClientCommandSource> PERMISSION_LEVEL_2 =
		//? if <=1.21.10 {
		source -> source.getPlayer().hasPermissions(2);
		 //?} else {
		/*Commands.hasPermission(new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER));
		*///?}

	public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
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
