package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import tools.redstone.redstonetools.ClientCommands;

//? if >=26.1 {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
//? } else {
/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
*///? }

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
						null))
					.then(argument("count", IntegerArgumentType.integer(1))
						.executes(context -> this.execute(
							context,
							registryAccess,
							ItemArgument.getItem(context, "item"),
							IntegerArgumentType.getInteger(context, "count"))))));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, CommandBuildContext registryAccess, ItemInput itemArgument, Integer count) {
		//? if <26.1 {
		/*String itemString = itemArgument.serialize(registryAccess);
		*///? } else {
		String itemString;
		if (count != null) {
			itemString = context.getInput().substring(2, context.getInput().length() - 1 - Integer.toString(count).length());
		} else {
			itemString = context.getInput().substring(2);
		}
		//? }

		if (count == null) count = 1;

		context.getSource().getPlayer().connection.sendCommand(
			"give @s " + itemString + " " + count
		);
		return 0;
	}
}
