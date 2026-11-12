package tools.redstone.redstonetools;

//? fabric {
/*import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
*///? } else {
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.server.MinecraftServer;
//? }
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.*;
import tools.redstone.redstonetools.utils.DependencyLookup;

import net.minecraft.commands.CommandSourceStack;

import java.util.function.Predicate;

public class Commands {
	//? paper
	public static MinecraftServer server; // do not use on fabric

	public static void registerCommands(/*? paper {*/io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager<org.bukkit.plugin.Plugin> events/*? }*/) {
		//? if fabric {
		/*CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
		 *///? } else
		events.registerEventHandler(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, event -> {
			//? paper {
			MinecraftServer server = ((org.bukkit.craftbukkit.CraftServer) org.bukkit.Bukkit.getServer()).getServer();
			Commands.server = server;
			var commands = server.getCommands();

			var commandDispatcher = commands.getDispatcher();
			var commandRegistryAccess = CommandBuildContext.simple(VanillaRegistries.createLookup(), server.getWorldData().enabledFeatures());
			var registrationEnvironment = net.minecraft.commands.Commands.CommandSelection.DEDICATED;
			//? }

			if (DependencyLookup.WORLDEDIT_PRESENT) {
				BinaryBlockReadFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				ColorCodeFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				MinSelectionFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				RStackFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				SelectionStackFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				FindFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				SignSearchFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				ThatFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			}
			ReachFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			BaseConvertFeature.INSTANCE.registerCommand(commandDispatcher);
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
			SlabFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			PinFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			WorldEditHelperFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
		});
	}

	public static Predicate<CommandSourceStack> getPerm(String s) {
		//? if fabric {
		/*//? if <=1.21.10 {
		/^return source -> source.hasPermission(2);
		^///?} else {
		return net.minecraft.commands.Commands.hasPermission(new net.minecraft.server.permissions.PermissionCheck.Require(net.minecraft.server.permissions.Permissions.COMMANDS_GAMEMASTER));
		//?}
		*///? } else {
		return sender -> sender.getSender().hasPermission("redstonetools." + s);
		//? }
	}
}
