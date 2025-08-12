package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ItemBindFeature extends AbstractFeature {
	public static void registerCommand() {
		EVENT.register((dispatcher, registryAccess, enviroment) -> dispatcher.register(literal("itembind")
				.executes(context -> FeatureUtils.getFeature(ItemBindFeature.class).execute(context))
				.then(argument("reset", BoolArgumentType.bool())
						.executes(context -> {
							if (BoolArgumentType.getBool(context, "reset")) {
								var player = context.getSource().getPlayer();
								if (player != null) {
									var stack = player.getMainHandStack();
									boolean mainhand = stack == ItemStack.EMPTY;
									if (!mainhand) {
										stack = player.getOffHandStack();
									}
									stack.remove(RedstoneTools.COMMAND_COMPONENT);
									stack.set(DataComponentTypes.LORE, stack.getItem().getDefaultStack().get(DataComponentTypes.LORE));
									context.getSource().getPlayer().sendMessage(Text.of("Successfully removed command from the item in your " + (mainhand ? "hand" : "offhand")), false);
								} else {
									context.getSource().getPlayer().sendMessage(Text.of("You need to be holding an item in one of your hands!"), false);
								}
								return 1;
							} else {
								return FeatureUtils.getFeature(ItemBindFeature.class).execute(context);
							}
						}))));
	}

	public static ArrayList<ServerPlayerEntity> playersWaitingForCommand = new ArrayList<>();

	protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
//        if (!Objects.requireNonNull(context.getSource().getPlayer()).getGameMode().isCreative()) {
//            throw new SimpleCommandExceptionType(Text.literal("You must be in creative to use this command!")).create();
//        }
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
		playerI.sendMessage(Text.literal("Successfully bound command: '%s' to this item (%s)!".formatted(command, stack.getItem().getName().getString())), false);

	}

	public static boolean waitingForCommandForPlayer(ServerPlayerEntity player1) {
		return playersWaitingForCommand.contains(player1);
	}
}

