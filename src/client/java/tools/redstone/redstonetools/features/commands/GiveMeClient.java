package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import tools.redstone.redstonetools.ClientCommands;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class GiveMeClient {
	public static final GiveMeClient INSTANCE = new GiveMeClient();

	protected GiveMeClient() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(
			literal("g")
				.requires(ClientCommands.PERMISSION_LEVEL_2)
				.then(argument("item", ItemArgument.item(registryAccess))
					.executes(context -> this.execute(
						context,
						registryAccess,
						ItemArgument.getItem(context, "item"),
						1))
					.then(argument("count", IntegerArgumentType.integer(1))
						.executes(context -> this.execute(
							context,
							registryAccess,
							ItemArgument.getItem(context, "item"),
							IntegerArgumentType.getInteger(context, "count"))))));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, CommandBuildContext registryAccess, ItemInput itemArgument, int count) {
		context.getSource().getPlayer().connection.sendCommand(
			"give @s " + itemArgument.serialize(registryAccess) + " " + count
		);
		return 0;
	}
}
