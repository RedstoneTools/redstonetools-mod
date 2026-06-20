package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.DirectionArgument;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static tools.redstone.redstonetools.utils.DirectionUtils.directionToBlock;
import static tools.redstone.redstonetools.utils.DirectionUtils.matchDirection;

public class RStackFeature {
	public static final RStackFeature INSTANCE = new RStackFeature();

	protected RStackFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(
				literal("/rstack")
					.requires(Commands.PERMISSION_LEVEL_2)
					.executes(getCommandForArgumentCount(0))
					.then(argument("count", IntegerArgumentType.integer())
						.executes(getCommandForArgumentCount(1))
						.then(argument("direction", StringArgumentType.string()).suggests(ArgumentUtils.DIRECTION_SUGGESTION_PROVIDER)
							.executes(getCommandForArgumentCount(2))
							.then(argument("offset", IntegerArgumentType.integer())
								.executes(getCommandForArgumentCount(3))
								.then(argument("moveSelection", BoolArgumentType.bool())
									.executes(getCommandForArgumentCount(4)))))));
	}

	protected Command<CommandSourceStack> getCommandForArgumentCount(int argNum) {
		return context -> execute(context, argNum);
	}

	protected int execute(CommandContext<CommandSourceStack> context, int argCount) throws CommandSyntaxException {
		int count = argCount >= 1 ? IntegerArgumentType.getInteger(context, "count") : 1;
		DirectionArgument direction = argCount >= 2 ? ArgumentUtils.parseDirection(context, "direction") : DirectionArgument.ME;
		int offset = argCount >= 3 ? IntegerArgumentType.getInteger(context, "offset") : 2;
		boolean moveSelection = argCount >= 4 && BoolArgumentType.getBool(context, "moveSelection");
		return execute(context, count, offset, direction, moveSelection);
	}

	protected int execute(CommandContext<CommandSourceStack> context, int count, int offset, DirectionArgument direction, boolean moveSelection) throws CommandSyntaxException {
		//? if <26.1 {
		var actor = FabricAdapter.adaptPlayer(Objects.requireNonNull(context.getSource().getPlayer()));
		 //? } else
		//var actor = FabricAdapter.get().fromNativePlayer(Objects.requireNonNull(context.getSource().getPlayer()));

		var localSession = WorldEdit.getInstance()
				.getSessionManager()
				.get(actor);

		final var selectionWorld = localSession.getSelectionWorld();
		assert selectionWorld != null;

		final Region selection;
		try {
			selection = localSession.getSelection(selectionWorld);
		} catch (IncompleteRegionException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Please make a selection with WorldEdit first.")).create();
		}

		final Mask airFilter = new Mask() {
			@Override
			public boolean test(BlockVector3 vector) {
				return !"minecraft:air".equals(selectionWorld.getBlock(vector).getBlockType().id());
			}

			@Nullable
			@Override
			public Mask2D toMask2D() {
				return null;
			}
		};

		var playerFacing = actor.getLocation().getDirectionEnum();
		Direction stackDirection;
		try {
			stackDirection = matchDirection(direction, playerFacing);
		} catch (Exception e) {
			throw new SimpleCommandExceptionType(Component.literal(e.getMessage().formatted(e))).create();
		}
		var stackVector = directionToBlock(stackDirection);


		try (var editSession = localSession.createEditSession(actor)) {
			for (var i = 1; i <= count; i++) {
				BlockVector3 offsetVector = Objects.requireNonNull(stackVector).multiply(i * offset);
				var copy = new ForwardExtentCopy(
						editSession,
						selection,
						editSession,
						selection.getMinimumPoint().add(offsetVector)
				);
				copy.setSourceMask(airFilter);
				copy.setSourceFunction(position -> false);
				Operations.complete(copy);
				if (i == count && moveSelection) {
					selection.shift(offsetVector);
				}
			}
			localSession.remember(editSession);
		} catch (WorldEditException e) {
			throw new RuntimeException(e);
		}

		context.getSource().sendSystemMessage(Component.literal("Stacked %s time(s).".formatted(count)));
		return 1;
	}
}
