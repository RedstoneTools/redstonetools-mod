package tools.redstone.redstonetools.features.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.network.chat.Component;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.config.ServerConfig;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.BlockSet;
import tools.redstone.redstonetools.utils.TickScheduler;
import tools.redstone.redstonetools.utils.WorldEditUtils;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ThatFeature {

	public static final ThatFeature INSTANCE = new ThatFeature();

	private static final String DEFAULT_MASK = "#existing";
	private static final int ITERATIONS_PER_BURST = 2000;

	protected ThatFeature() {
	}

	public void registerCommand(
		CommandDispatcher<CommandSourceStack> dispatcher,
		CommandBuildContext registryAccess,
		CommandSelection registrationEnvironment)
	{
		var node = dispatcher.register(literal("/that")
			.requires(Commands.getPerm("that"))
			.executes(context -> execute(context, Offsets.DEFAULT, DEFAULT_MASK))
			.then(diagonal("-d", Offsets.DIAG))
			.then(diagonal("-dd", Offsets.VERY_DIAG))
			.then(diagonal("-ddd", Offsets.VERY_VERY_DIAG))
			.then(maskArgument(Offsets.DEFAULT)));
	}

	private LiteralArgumentBuilder<CommandSourceStack> diagonal(String flag, List<BlockVector3> offsets) {
		return literal(flag)
			.executes(context -> execute(context, offsets, DEFAULT_MASK))
			.then(maskArgument(offsets));
	}

	private RequiredArgumentBuilder<CommandSourceStack, String> maskArgument(List<BlockVector3> offsets) {
		return argument("mask", StringArgumentType.greedyString())
			.suggests(ArgumentUtils.MASK_SUGGESTION_PROVIDER)
			.executes(context -> execute(context, offsets, StringArgumentType.getString(context, "mask")));
	}

	protected int execute(
		CommandContext<CommandSourceStack> context,
		List<BlockVector3> offsets,
		String maskInput)
		throws CommandSyntaxException
	{
		var serverPlayer = context.getSource().getPlayerOrException();
		var actor = WorldEditUtils.getActor(serverPlayer);
		var session = WorldEditUtils.getSession(serverPlayer);
		var mask = WorldEditUtils.parseMask(serverPlayer, maskInput);

		var trace = actor.getBlockTrace(ServerConfig.THAT_SIZE_LIMIT.get(), false, mask);
		if (trace == null) {
			throw new SimpleCommandExceptionType(Component.literal("No build in sight!")).create();
		}

		new Expansion(actor, session, mask, offsets, trace.toVector().toBlockPoint()).start();
		return 1;
	}

	/** One incremental flood fill, spread over several ticks. */
	private static class Expansion {

		private final Player actor;
		private final LocalSession session;
		private final Mask mask;
		private final List<BlockVector3> offsets;

		private final BlockSet visited = new BlockSet();
		private final Deque<BlockVector3> queue = new ArrayDeque<>();

		private final int sizeLimit = ServerConfig.THAT_SIZE_LIMIT.get();
		private final long maxNsPerTick = ServerConfig.THAT_MAX_TIME_PER_TICK_MS.get() * 1_000_000L;
		private final int maxTicks = ServerConfig.THAT_MAX_TICKS.get();

		private BlockVector3 min;
		private BlockVector3 max;
		private int ticks;

		Expansion(Player actor, LocalSession session, Mask mask, List<BlockVector3> offsets, BlockVector3 target) {
			this.actor = actor;
			this.session = session;
			this.mask = mask;
			this.offsets = offsets;
			this.min = target;
			this.max = target;

			queue.add(target);
			visited.add(target);
		}

		void start() {
			next();
		}

		private void next() {
			ticks++;

			long startNs = System.nanoTime();
			boolean sizeLimitReached = false;

			while (System.nanoTime() - startNs <= maxNsPerTick && !sizeLimitReached && !queue.isEmpty()) {
				expand();

				BlockVector3 size = max.subtract(min);
				sizeLimitReached = size.x() > sizeLimit || size.y() > sizeLimit || size.z() > sizeLimit;
			}

			if (queue.isEmpty()) {
				finish();
				return;
			}

			if (sizeLimitReached) {
				fail("Size");
				return;
			}

			if (ticks > maxTicks) {
				fail("Time");
				return;
			}

			TickScheduler.runNextTick(this::next);
		}

		private void expand() {
			for (int i = 0; i < ITERATIONS_PER_BURST && !queue.isEmpty(); i++) {
				BlockVector3 pos = queue.removeFirst();
				min = min.getMinimum(pos);
				max = max.getMaximum(pos);

				for (BlockVector3 offset : offsets) {
					BlockVector3 neighbour = pos.add(offset);

					if (visited.contains(neighbour)) {
						continue;
					}

					visited.add(neighbour);

					if (mask.test(neighbour)) {
						queue.addLast(neighbour);
					}
				}
			}
		}

		private void finish() {
			var world = actor.getWorld();
			var selector = new CuboidRegionSelector(world, min, max);

			session.setRegionSelector(world, selector);
			selector.explainRegionAdjust(actor, session);
			actor.printInfo(TextComponent.of("Build selected."));
		}

		private void fail(String kind) {
			actor.printError(TextComponent.of(kind + " limit exceeded. Your selection was not changed."));
		}
	}

	private static class Offsets {
		public static final ImmutableList<BlockVector3> DEFAULT = ImmutableList.of(
			v(1, 0, 0), v(-1, 0, 0),
			v(0, 1, 0), v(0, -1, 0),
			v(0, 0, 1), v(0, 0, -1));

		public static final ImmutableList<BlockVector3> DIAG = concat(DEFAULT,
			v(1, 1, 0),  v(-1, 1, 0),  v(0, 1, 1),  v(0, 1, -1),
			v(1, -1, 0), v(-1, -1, 0), v(0, -1, 1), v(0, -1, -1));

		public static final ImmutableList<BlockVector3> VERY_DIAG = concat(DIAG,
			v(1, 0, 1), v(-1, 0, 1), v(1, 0, -1), v(-1, 0, -1));

		public static final ImmutableList<BlockVector3> VERY_VERY_DIAG = concat(VERY_DIAG,
			v(1, 1, 1),  v(-1, 1, 1),  v(1, 1, -1),  v(-1, 1, -1),
			v(1, -1, 1), v(-1, -1, 1), v(1, -1, -1), v(-1, -1, -1));

		private static BlockVector3 v(int x, int y, int z) {
			return BlockVector3.at(x, y, z);
		}

		private static ImmutableList<BlockVector3> concat(List<BlockVector3> base, BlockVector3... extra) {
			ImmutableList.Builder<BlockVector3> builder = ImmutableList.builder();
			builder.addAll(base);
			builder.add(extra);
			return builder.build();
		}
	}
}
