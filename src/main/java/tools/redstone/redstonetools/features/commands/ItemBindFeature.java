package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.ItemUtils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import static net.minecraft.commands.Commands.literal;

public class ItemBindFeature {
	public static final ItemBindFeature INSTANCE = new ItemBindFeature();

	protected ItemBindFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(literal("itembind")
				.requires(Commands.PERMISSION_LEVEL_2)
				.executes(this::execute)
				.then(literal("reset")
					.executes(ItemBindFeature::executeReset)));
	}

	private static int executeReset(CommandContext<CommandSourceStack> context) {
		var player = context.getSource().getPlayer();
		if (player == null) {
			return 0;
		}
		boolean mainhand;
		if (ItemUtils.containsCommand(player.getMainHandItem())) {
			mainhand = true;
		} else if (ItemUtils.containsCommand(player.getOffhandItem())) {
			mainhand = false;
		} else {
			//? if <26.1 {
			context.getSource().getPlayer().displayClientMessage(Component.nullToEmpty("You need to be holding an item with a command in one of your hands!"), false);
			//? } else
			//context.getSource().getPlayer().sendSystemMessage(Component.nullToEmpty("You need to be holding an item with a command in one of your hands!"), false);
			return 0;
		}
		ItemStack stack = mainhand ? player.getMainHandItem() : player.getOffhandItem();
		ItemUtils.removeCommand(stack);
		stack.set(DataComponents.LORE, stack.getItem().getDefaultInstance().get(DataComponents.LORE));
		//? if <26.1 {
		context.getSource().getPlayer().displayClientMessage(Component.nullToEmpty("Successfully removed command from the item in your " + (mainhand ? "mainhand" : "offhand")), false);
		//? } else
		//context.getSource().getPlayer().sendSystemMessage(Component.nullToEmpty("Successfully removed command from the item in your " + (mainhand ? "mainhand" : "offhand")), false);
		return 1;
	}

	public static ArrayList<ServerPlayer> playersWaitingForCommand = new ArrayList<>();

	protected int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSourceStack source = context.getSource();
		playersWaitingForCommand.add(source.getPlayer());

		source.sendSystemMessage(Component.literal("Please run any command and hold the item you want the command be bound to in your main hand"));
		return 1;
	}

	public static void addCommand(String command, ServerPlayer playerI) {
		if (!waitingForCommandForPlayer(playerI)) return;

		ItemStack stack = playerI.getMainHandItem();
		if (stack.isEmpty()) {
			if (playerI.getOffhandItem().isEmpty()) {
				playerI.sendSystemMessage(Component.literal("You need to be holding an item!"));
				return;
			} else
				stack = playerI.getOffhandItem();
		}

		ItemUtils.setCommand(stack, command);
		//                                                                                                   `command` here doesn't start with a / for some
		//                                                                                                   reason, so we add it ourselves so its more clear
		stack.set(DataComponents.LORE, new ItemLore(List.of(Component.nullToEmpty("Has command: /" + command))));

		playersWaitingForCommand.remove(playerI);
		//? if <26.1 {
		playerI.displayClientMessage(Component.literal("Successfully bound command: '/%s' to this item (%s)!".formatted(command, stack.getItem().getName().getString())), false);
		//? } else
		//playerI.sendSystemMessage(Component.literal("Successfully bound command: '/%s' to this item (%s)!".formatted(command, stack.getItem().getName(stack).getString())), false);

	}

	public static boolean waitingForCommandForPlayer(ServerPlayer player1) {
		return playersWaitingForCommand.contains(player1);
	}
}

