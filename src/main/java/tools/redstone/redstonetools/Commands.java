package tools.redstone.redstonetools;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
//? if >=1.21.11 {
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;
//? }
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.*;
import tools.redstone.redstonetools.utils.DependencyLookup;

import java.util.function.Predicate;

public class Commands {
	public static final Predicate<CommandSourceStack> PERMISSION_LEVEL_2 =
		//? if <=1.21.10 {
		/*source -> source.hasPermission(2);
		*///?} else {
		net.minecraft.commands.Commands.hasPermission(new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER));
		//?}

	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
			if (DependencyLookup.WORLDEDIT_PRESENT) {
				BinaryBlockReadFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				ColorCodeFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				MinSelectionFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				RStackFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			}
			ReachFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			BaseConvertFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			GiveMeFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			ItemComponentsFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			ItemBindFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			QuickTpFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			SignalStrengthBlockFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			AutoDustFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			AutoRotateFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			ClickContainerFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			ColoredFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			CopyStateFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
		});
	}
}
