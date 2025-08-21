package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.RedstoneTools;

import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT;
import static net.minecraft.server.command.CommandManager.literal;

public class ItemBindFeature {
	public static final ItemBindFeature INSTANCE = new ItemBindFeature();

	protected ItemBindFeature() {
	}

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess, enviroment) ->
			dispatcher.register(literal("itembind")
				.executes(this::execute)
				.then(literal("reset")
					.executes(ItemBindFeature::executeReset))));
	}

	private static int executeReset(CommandContext<ServerCommandSource> context) {
		var player = context.getSource().getPlayer();
		if (player == null) {
			return 0;
		}
		boolean mainhand;
		if (player.getMainHandStack().get(RedstoneTools.COMMAND_COMPONENT) != null) {
			mainhand = true;
		} else if (player.getOffHandStack().get(RedstoneTools.COMMAND_COMPONENT) != null) {
			mainhand = false;
		} else {
			context.getSource().getPlayer().sendMessage(Text.of("You need to be holding an item with a command in one of your hands!"), false);
			return 0;
		}
		ItemStack stack = mainhand ? player.getMainHandStack() : player.getOffHandStack();
		stack.remove(RedstoneTools.COMMAND_COMPONENT);
		stack.set(DataComponentTypes.LORE, stack.getItem().getDefaultStack().get(DataComponentTypes.LORE));
		context.getSource().getPlayer().sendMessage(Text.of("Successfully removed command from the item in your " + (mainhand ? "mainhand" : "offhand")), false);
		return 1;
	}

	public static ArrayList<ServerPlayerEntity> playersWaitingForCommand = new ArrayList<>();

	protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
//		if (!Objects.requireNonNull(context.getSource().getPlayer()).getGameMode().isCreative()) {
//			throw new SimpleCommandExceptionType(Text.literal("You must be in creative to use this command!")).create();
//		}
		ServerCommandSource source = context.getSource();
		playersWaitingForCommand.add(source.getPlayer());

		source.sendMessage(Text.literal("Please run any command and hold the item you want the command be bound to in your main hand"));
		return 1;
	}

	public static void addCommand(String command, ServerPlayerEntity playerI) {
		if (!waitingForCommandForPlayer(playerI)) return;

		ItemStack stack = playerI.getMainHandStack();
		if (stack.isEmpty()) {
			if (playerI.getOffHandStack().isEmpty()) {
				playerI.sendMessage(Text.literal("You need to be holding an item!"));
				return;
			} else
				stack = playerI.getOffHandStack();
		}

		stack.set(RedstoneTools.COMMAND_COMPONENT, command);
		//                                                                                                   `command` here doesn't start with a / for some
		//                                                                                                   reason, so we add it ourselves so its more clear
		stack.set(DataComponentTypes.LORE, new LoreComponent(List.of(Text.of("Has command: /" + command))));

		playersWaitingForCommand.remove(playerI);
		playerI.sendMessage(Text.literal("Successfully bound command: '/%s' to this item (%s)!".formatted(command, stack.getItem().getName().getString())), false);

	}

	public static boolean waitingForCommandForPlayer(ServerPlayerEntity player1) {
		return playersWaitingForCommand.contains(player1);
	}
}

