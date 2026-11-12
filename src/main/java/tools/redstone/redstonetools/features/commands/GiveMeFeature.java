package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;

import net.minecraft.commands.CommandSourceStack;
//? if fabric {
/*import net.minecraft.commands.CommandBuildContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
*///? } else {
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
//? }

import java.util.List;
import java.util.Objects;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class GiveMeFeature {
	public static final GiveMeFeature INSTANCE = new GiveMeFeature();

	protected GiveMeFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(
			literal("g")
				.requires(tools.redstone.redstonetools.Commands.getPerm("g"))
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
		//? if <26.1 {
		/*var server = context.getSource().getServer();
		//? if <26.1 {
		/^var stack = itemArgument.createItemStack(1, false);
		^///? } else
		var stack = itemArgument.createItemStack(1);
		stack.setCount(count);
		server.getCommands().performPrefixedCommand(
			server.createCommandSourceStack(), "/give " + context.getSource().getTextName() + " " + itemArgument.serialize(server.registryAccess()) + " " + count);
		*///? } else {
		//? if fabric {
		/*tools.redstone.redstonetools.mixin.accessor.GiveCommandAccessor.invokeGiveItem(context.getSource(), itemArgument, List.of(Objects.requireNonNull(context.getSource().getPlayer())), count);
		 *///? } else {
		ServerPlayer player = context.getSource().getPlayerOrException();
		int maxStackSize = itemArgument.item().value().getDefaultMaxStackSize();
		int remaining = count;

		while (remaining > 0) {
			int amount = Math.min(maxStackSize, remaining);
			remaining -= amount;

			ItemStack stack = itemArgument.createItemStack(amount);
			if (!player.getInventory().add(stack) && !stack.isEmpty()) {
				ItemEntity dropped = player.drop(stack, false);
				if (dropped != null) {
					dropped.setNoPickUpDelay();
					dropped.setTarget(player.getUUID());
				}
			}
		}

		player.inventoryMenu.broadcastChanges();
		//? }
		//? }
		return 0;
	}
}