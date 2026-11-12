package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.BlockBreakCapture;
import tools.redstone.redstonetools.utils.TickScheduler;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class PinFeature {

	public static final PinFeature INSTANCE = new PinFeature();

	private final Map<UUID, Map<String, Pin>> pins = new HashMap<>();

	protected PinFeature() {
	}

	private record Pin(ResourceKey<Level> level, BlockPos pos) {
	}

	private final SuggestionProvider<CommandSourceStack> pinNames = (context, builder) -> {
		ServerPlayer player = context.getSource().getPlayer();

		if (player != null) {
			SharedSuggestionProvider.suggest(pinsOf(player).keySet(), builder);
		}

		return builder.buildFuture();
	};

	public void registerCommand(
		CommandDispatcher<CommandSourceStack> dispatcher,
		CommandBuildContext registryAccess,
		CommandSelection registrationEnvironment) {
		dispatcher.register(literal("pin")
			.requires(Commands.getPerm("pin"))
			.then(literal("add")
				.then(argument("name", StringArgumentType.word())
					.executes(this::add)))
			.then(literal("remove")
				.then(argument("name", StringArgumentType.word())
					.suggests(pinNames)
					.executes(this::remove)))
			.then(literal("turn")
				.then(literal("on")
					.then(argument("name", StringArgumentType.word())
						.suggests(pinNames)
						.executes(context -> turn(context, true))))
				.then(literal("off")
					.then(argument("name", StringArgumentType.word())
						.suggests(pinNames)
						.executes(context -> turn(context, false)))))
			.then(literal("toggle")
				.then(argument("name", StringArgumentType.word())
					.suggests(pinNames)
					.executes(this::toggle)))
			.then(literal("pulse")
				.then(literal("on").then(pulseArguments(true)))
				.then(literal("off").then(pulseArguments(false))))
			.then(literal("list")
				.executes(this::list)));
	}

	private RequiredArgumentBuilder<CommandSourceStack, String> pulseArguments(boolean powered) {
		return argument("name", StringArgumentType.word())
			.suggests(pinNames)
			.then(argument("ticks", IntegerArgumentType.integer(1, 100))
				.executes(context -> pulse(context, powered)));
	}

	private Map<String, Pin> pinsOf(ServerPlayer player) {
		return pins.computeIfAbsent(player.getUUID(), uuid -> new LinkedHashMap<>());
	}

	protected int add(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		String name = StringArgumentType.getString(context, "name");

		if (pinsOf(player).containsKey(name)) {
			throw new SimpleCommandExceptionType(
				Component.literal("Pin " + name + " already exists")).create();
		}

		boolean claimed = BlockBreakCapture.claimNextBreak(
			player, (breaker, pos) -> onLeverPicked(breaker, name, pos));

		if (!claimed) {
			throw new SimpleCommandExceptionType(
				Component.literal("You are already adding a pin")).create();
		}

		context.getSource().sendSystemMessage(
			Component.literal("Break the lever you want to pin as " + name));
		return 1;
	}

	private void onLeverPicked(ServerPlayer player, String name, BlockPos pos) {
		if (!(player.level().getBlockState(pos).getBlock() instanceof LeverBlock)) {
			player.sendSystemMessage(
				Component.literal("That is not a lever. Run /pin add " + name + " again."));
			return;
		}

		pinsOf(player).put(name, new Pin(player.level().dimension(), pos));
		player.sendSystemMessage(Component.literal("Pin " + name + " added"));
	}

	protected int remove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		String name = StringArgumentType.getString(context, "name");

		if (pinsOf(player).remove(name) == null) {
			throw new SimpleCommandExceptionType(
				Component.literal("No pin named " + name)).create();
		}

		context.getSource().sendSystemMessage(Component.literal("Pin " + name + " removed"));
		return 1;
	}

	protected int list(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		Map<String, Pin> owned = pinsOf(player);

		if (owned.isEmpty()) {
			context.getSource().sendSystemMessage(Component.literal("You have no pins"));
			return 0;
		}

		context.getSource().sendSystemMessage(Component.literal("Your pins:"));
		owned.forEach((name, pin) -> context.getSource().sendSystemMessage(
			Component.literal(name + " at " + pin.pos().toShortString())));

		return owned.size();
	}

	private ServerLevel levelOf(MinecraftServer server, Pin pin) {
		return server.getLevel(pin.level());
	}

	private BlockState leverState(MinecraftServer server, Pin pin) {
		ServerLevel level = levelOf(server, pin);

		if (level == null) {
			return null;
		}

		BlockState state = level.getBlockState(pin.pos());
		return state.getBlock() instanceof LeverBlock ? state : null;
	}

	private boolean applyState(Player player, MinecraftServer server, Pin pin, boolean powered) {
		BlockState state = leverState(server, pin);

		if (state == null) {
			return false;
		}

		if (state.getValue(BlockStateProperties.POWERED) != powered) {
			ServerLevel level = levelOf(server, pin);
			BlockState updated = state.setValue(BlockStateProperties.POWERED, powered);

			boolean canceled = false;
			//? paper {
			var event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent(
				player,
				org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK,
				pin.pos(),
				Direction.DOWN,
				ItemStack.EMPTY,
				InteractionHand.MAIN_HAND);
			//? }
			if (!canceled) {
				level.setBlock(pin.pos(), updated, Block.UPDATE_ALL);
				updateAttachedBlock(level, pin.pos(), updated);
			}
		}

		return true;
	}

	/** Reimplements FaceAttachedHorizontalDirectionalBlock.getConnectedDirection, which is protected. */
	private Direction connectedDirection(BlockState state) {
		return switch (state.getValue(BlockStateProperties.ATTACH_FACE)) {
			case CEILING -> Direction.DOWN;
			case FLOOR -> Direction.UP;
			case WALL -> state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		};
	}

	private void updateAttachedBlock(ServerLevel level, BlockPos pos, BlockState state) {
		BlockPos attached = pos.relative(connectedDirection(state).getOpposite());
		level.updateNeighborsAt(attached, state.getBlock());
	}

	private Pin requirePin(ServerPlayer player, String name) throws CommandSyntaxException {
		Pin pin = pinsOf(player).get(name);

		if (pin == null) {
			throw new SimpleCommandExceptionType(Component.literal("No pin named " + name)).create();
		}

		return pin;
	}

	private CommandSyntaxException destroyed(String name) {
		return new SimpleCommandExceptionType(
			Component.literal("Pin " + name + " has been destroyed")).create();
	}

	protected int turn(CommandContext<CommandSourceStack> context, boolean powered) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		String name = StringArgumentType.getString(context, "name");

		if (!applyState(player, context.getSource().getServer(), requirePin(player, name), powered)) {
			throw destroyed(name);
		}

		context.getSource().sendSystemMessage(
			Component.literal("Turned " + name + (powered ? " on" : " off")));
		return 1;
	}

	protected int toggle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		String name = StringArgumentType.getString(context, "name");
		Pin pin = requirePin(player, name);

		BlockState state = leverState(context.getSource().getServer(), pin);
		if (state == null) {
			throw destroyed(name);
		}

		boolean powered = !state.getValue(BlockStateProperties.POWERED);
		applyState(player, context.getSource().getServer(), pin, powered);

		context.getSource().sendSystemMessage(
			Component.literal("Toggled " + name + (powered ? " on" : " off")));
		return 1;
	}

	protected int pulse(CommandContext<CommandSourceStack> context, boolean powered) throws CommandSyntaxException {
		MinecraftServer server = context.getSource().getServer();
		ServerPlayer player = context.getSource().getPlayerOrException();
		String name = StringArgumentType.getString(context, "name");
		int redstoneTicks = IntegerArgumentType.getInteger(context, "ticks");
		Pin pin = requirePin(player, name);
		UUID owner = player.getUUID();

		if (!applyState(player, server, pin, powered)) {
			throw destroyed(name);
		}

		// A redstone tick is two game ticks.
		TickScheduler.runLater(() -> {
			if (applyState(player, server, pin, !powered)) {
				return;
			}

			ServerPlayer stillOnline = server.getPlayerList().getPlayer(owner);
			if (stillOnline != null) {
				stillOnline.sendSystemMessage(Component.literal("Pin " + name + " was destroyed mid-pulse"));
			}
		}, redstoneTicks * 2L);

		context.getSource().sendSystemMessage(Component.literal(
			"Pulsed " + name + (powered ? " on" : " off") + " for " + redstoneTicks + " redstone ticks"));
		return 1;
	}
}
