package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GiveMeFeature {
	public static final GiveMeFeature INSTANCE = new GiveMeFeature();

	protected GiveMeFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
		dispatcher.register(
			literal("g")
				.requires(source -> source.hasPermissionLevel(2))
				.then(argument("item", ItemStackArgumentType.itemStack(registryAccess))
					.executes(context -> this.execute(
						context,
						ItemStackArgumentType.getItemStackArgument(context, "item"),
						1))
					.then(argument("count", IntegerArgumentType.integer(1))
						.executes(context -> this.execute(
							context,
							ItemStackArgumentType.getItemStackArgument(context, "item"),
							IntegerArgumentType.getInteger(context, "count"))))));
	}

	private int execute(CommandContext<ServerCommandSource> context, ItemStackArgument itemArgument, int count) throws CommandSyntaxException {
		MinecraftServer server = context.getSource().getServer();
		ItemStack stack = itemArgument.createStack(1, false);
		stack.setCount(count);
		server.getCommandManager()./*? if <1.21.10 {*//*executeWithPrefix*//*?} else {*/parseAndExecute/*?}*/(
			server.getCommandSource(), "/give @s " + itemArgument.asString(server.getRegistryManager()) + " " + count);
		return 0;
	}
}
