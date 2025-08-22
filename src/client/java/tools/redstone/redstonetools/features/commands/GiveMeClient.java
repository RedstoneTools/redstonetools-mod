package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class GiveMeClient {
	public static final GiveMeClient INSTANCE = new GiveMeClient();

	protected GiveMeClient() {
	}

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
			literal("g")
				.requires(source -> source.getPlayer().hasPermissionLevel(2))
				.then(argument("item", ItemStackArgumentType.itemStack(registryAccess))
					.executes(context -> this.execute(
						context,
						registryAccess,
						ItemStackArgumentType.getItemStackArgument(context, "item"),
						1))
					.then(argument("count", IntegerArgumentType.integer(1))
						.executes(context -> this.execute(
							context,
							registryAccess,
							ItemStackArgumentType.getItemStackArgument(context, "item"),
							IntegerArgumentType.getInteger(context, "count")))))));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, CommandRegistryAccess registryAccess, ItemStackArgument itemArgument, int count) {
		context.getSource().getPlayer().networkHandler.sendChatCommand(
			"give @s " + itemArgument.asString(registryAccess) + " " + count
		);
		return 0;
	}
}
