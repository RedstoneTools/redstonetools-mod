package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.limit.PermissiveSelectorLimits;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.*;

import static net.minecraft.commands.Commands.literal;

public class SelectionStackFeature {
	public static final SelectionStackFeature INSTANCE = new SelectionStackFeature();

	protected SelectionStackFeature() {
	}

	private record StackEntry(BlockVector3 pos1, BlockVector3 pos2) {

	}

	// TODO: entries are never removed when a player disconnects, See the platform layer issue
	// Causes memory leaks, but easily fixed post refactor, so not to worry, but not to ship to prod either
	private final Map<UUID, List<StackEntry>> stacks = new HashMap<>();

	private List<StackEntry> stackOf(ServerPlayer player) {
		return stacks.computeIfAbsent(player.getUUID(), uuid -> new ArrayList<>());
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, CommandSelection registrationEnvironment) {
		dispatcher.register(literal("/selstack")
			.requires(Commands.getPerm("selstack"))
			.then(literal("push").executes(this::push))
			.then(literal("pop").executes(this::pop))
			.then(literal("clear").executes(this::clear))
			.then(literal("show").executes(this::show)));
	}

	protected int push(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var selection = WorldEditUtils.getSelection(player);
		var actor = WorldEditUtils.getActor(player);
		var session = WorldEditUtils.getSession(player);

		var boundingBox = selection.getBoundingBox();
		stackOf(player).add(new StackEntry(boundingBox.getPos1(), boundingBox.getPos2()));

		var selector = session.getRegionSelector(session.getSelectionWorld());
		selector.clear();
		selector.explainRegionAdjust(actor, session);

		context.getSource().sendSystemMessage(Component.literal("Selection pushed. Selection cleared."));
		return 1;
	}

	protected int pop(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var stack = stackOf(player);

		if (stack.isEmpty()) {
			context.getSource().sendSystemMessage(Component.literal("Your selection stack is empty"));
			return 0;
		}
		var entry = stack.removeLast();
		var actor = WorldEditUtils.getActor(player);
		var session = WorldEditUtils.getSession(player);

		var selector = session.getRegionSelector(actor.getWorld());
		selector.clear();
		selector.selectPrimary(entry.pos1(), PermissiveSelectorLimits.getInstance());
		selector.selectSecondary(entry.pos2(), PermissiveSelectorLimits.getInstance());
		selector.explainPrimarySelection(actor, session, entry.pos1());
		selector.explainSecondarySelection(actor, session, entry.pos2());

		context.getSource().sendSystemMessage(Component.literal("Selection popped."));
		return 1;
	}

	protected int clear(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		stackOf(player).clear();

		context.getSource().sendSystemMessage(Component.literal("Your selection stack has been cleared"));
		return 1;
	}

	protected int show(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var stack = stackOf(player);

		if (stack.isEmpty()) {
			context.getSource().sendSystemMessage(Component.literal("Your selection stack is empty"));
			return 0;
		}

		context.getSource().sendSystemMessage(Component.literal("Least recent"));
		context.getSource().sendSystemMessage(Component.literal("pos1 / pos2"));
		for (StackEntry entry : stack) {
			context.getSource().sendSystemMessage(
				Component.literal(
					WorldEditUtils.BV3ToString(entry.pos1()) + " / " + WorldEditUtils.BV3ToString(entry.pos2)));
		}
		context.getSource().sendSystemMessage(Component.literal("Most recent (top of stack)"));

		return stack.size();
	}
}