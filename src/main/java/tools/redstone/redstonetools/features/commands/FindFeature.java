package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.RegionMaskingFilter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.formatting.component.InvalidComponentException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.network.chat.Component;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.LocationContainer;
import tools.redstone.redstonetools.utils.LocationsPaginationBox;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class FindFeature {

	public static final FindFeature INSTANCE = new FindFeature();

	private static final int MAX_PRINTED_RESULTS = 100;

	// TODO: entries are never removed when a player disconnects. See the Platform layer issue.
	private final Map<UUID, List<LocationContainer>> results = new HashMap<>();

	protected FindFeature() {
	}

	public void registerCommand(
		CommandDispatcher<CommandSourceStack> dispatcher,
		CommandBuildContext registryAccess,
		CommandSelection registrationEnvironnement)
	{
		dispatcher.register(literal("/find")
			.then(literal("-p")
				.then(argument("page", IntegerArgumentType.integer(1))
					.executes(this::showPage)))
			.then(argument("mask", StringArgumentType.greedyString())
				.requires(Commands.getPerm("find"))
				.suggests(ArgumentUtils.MASK_SUGGESTION_PROVIDER)
				.executes(this::execute)));
	}

	protected int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var selection = WorldEditUtils.getSelection(player);

		WorldEditUtils.requireScannableVolume(selection);

		var mask = WorldEditUtils.parseMask(player, StringArgumentType.getString(context, "mask"));

		var matches = new ArrayList<LocationContainer>();
		RegionFunction collect = position -> {
			matches.add(LocationContainer.of(position));
			return false;
		};

		try {
			Operations.complete(new RegionVisitor(selection, new RegionMaskingFilter(mask, collect)));
		} catch (WorldEditException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Search failed: " + ex.getMessage())).create();
		}

		var actor = WorldEditUtils.getActor(player);

		if (matches.isEmpty()) {
			results.remove(player.getUUID());
			context.getSource().sendSystemMessage(Component.literal("No results found"));
			return 0;
		}

		results.put(player.getUUID(), List.copyOf(matches));
		context.getSource().sendSystemMessage(
			Component.literal(matches.size() + " result(s)"));
		sendPage(actor, matches, 1);

		return matches.size();
	}

	protected int showPage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var locations = results.get(player.getUUID());

		if (locations == null) {
			throw new SimpleCommandExceptionType(
				Component.literal("Use //find to get results first")).create();
		}

		sendPage(WorldEditUtils.getActor(player),
			locations,
			IntegerArgumentType.getInteger(context, "page"));
		return 1;
	}

	private void sendPage(Actor actor, List<LocationContainer> locations, int page) throws CommandSyntaxException {
		var box = new LocationsPaginationBox(locations, "Find Results", "//find -p %page%");

		try {
			actor.print(box.create(page));
		} catch (InvalidComponentException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Invalid page number.")).create();
		}
	}
}
