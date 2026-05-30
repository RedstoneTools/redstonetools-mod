package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import tools.redstone.redstonetools.Commands;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class GiveMeFeature {
	public static final GiveMeFeature INSTANCE = new GiveMeFeature();

	protected GiveMeFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(
			literal("g")
				.requires(Commands.PERMISSION_LEVEL_2)
				.then(argument("item", ItemArgument.item(registryAccess))
					.executes(context -> this.execute(
						context,
						ItemArgument.getItem(context, "item"),
						1))
					.then(argument("count", IntegerArgumentType.integer(1))
						.executes(context -> this.execute(
							context,
							ItemArgument.getItem(context, "item"),
							IntegerArgumentType.getInteger(context, "count"))))));
	}

	private int execute(CommandContext<CommandSourceStack> context, ItemInput itemArgument, int count) throws CommandSyntaxException {
		MinecraftServer server = context.getSource().getServer();
		ItemStack stack = itemArgument.createItemStack(1, false);
		stack.setCount(count);
		server.getCommands().performPrefixedCommand(
			server.createCommandSourceStack(), "/give " + context.getSource().getTextName() + " " + itemArgument.serialize(server.registryAccess()) + " " + count);
		return 0;
	}
}
