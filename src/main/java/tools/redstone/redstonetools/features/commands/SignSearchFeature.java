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
import com.sk89q.worldedit.function.mask.BlockCategoryMask;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.formatting.component.InvalidComponentException;
import com.sk89q.worldedit.world.block.BlockCategories;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.LocationContainer;
import tools.redstone.redstonetools.utils.LocationsPaginationBox;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

// TODO: front/back in results?
public class SignSearchFeature {

	public static final SignSearchFeature INSTANCE = new SignSearchFeature();

	// TODO: entries are never removed when a player disconnects. See the Platform layer issue.
	private final Map<UUID, List<LocationContainer>> results = new HashMap<>();

	protected SignSearchFeature() {
	}

	private record SignLine(String label, String text) {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, CommandSelection registrationEnvironment) {
		dispatcher.register(literal("/signsearch")
			.then(literal("-p")
				.then(argument("page", IntegerArgumentType.integer(1))
					.executes(this::showPage)))
			.then(argument("regex", StringArgumentType.greedyString())
				.requires(Commands.getPerm("signsearch"))
				.executes(this::execute)));
	}

	protected int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var level = context.getSource().getLevel();
		var selection = WorldEditUtils.getSelection(player);

		WorldEditUtils.requireScannableVolume(selection);

		var pattern = compile(StringArgumentType.getString(context, "regex"));
		var signMask = new BlockCategoryMask(selection.getWorld(), BlockCategories.ALL_SIGNS);

		var matches = new ArrayList<LocationContainer>();
		RegionFunction collect = position -> {
			var match = describeMatch(readSign(level, position), pattern);
			if (match != null) {
				matches.add(LocationContainer.of(position, match));
			}
			return false;
		};

		try {
			Operations.complete(new RegionVisitor(selection, new RegionMaskingFilter(signMask, collect)));
		} catch (WorldEditException ex) {
			throw new SimpleCommandExceptionType(
				Component.literal("Search failed: " + ex.getMessage())).create();
		}

		if (matches.isEmpty()) {
			results.remove(player.getUUID());
			context.getSource().sendSystemMessage(Component.literal("No results found"));
			return 0;
		}

		results.put(player.getUUID(), List.copyOf(matches));
		context.getSource().sendSystemMessage(Component.literal(matches.size() + " result(s)"));
		sendPage(WorldEditUtils.getActor(player), matches, 1);

		return matches.size();
	}

	protected int showPage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var locations = results.get(player.getUUID());

		if (locations == null) {
			throw new SimpleCommandExceptionType(
				Component.literal("Use //signsearch to get results first")).create();
		}

		sendPage(WorldEditUtils.getActor(player), locations, IntegerArgumentType.getInteger(context, "page"));
		return 1;
	}

	private void sendPage(Actor actor, List<LocationContainer> locations, int page) throws CommandSyntaxException {
		var box = new LocationsPaginationBox(
			locations,
			"Sign Search Results",
			"//signsearch -p %page%");

		try {
			actor.print(box.create(page));
		} catch (InvalidComponentException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Invalid page number.")).create();
		}
	}

	private Pattern compile(String regex) throws CommandSyntaxException {
		try {
			return Pattern.compile(regex);
		} catch (PatternSyntaxException ex) {
			throw new SimpleCommandExceptionType(
				Component.literal("Illegal pattern: " + ex.getMessage())).create();
		}
	}

	private List<SignLine> readSign(ServerLevel level, BlockVector3 position) {
		var blockEntity = level.getBlockEntity(new BlockPos(position.x(), position.y(), position.z()));
		if (!(blockEntity instanceof SignBlockEntity sign)) {
			return List.of();
		}

		var lines = new ArrayList<SignLine>();
		//~ if >=26.3 'false)' -> 'false).toArray(Component[]::new)' {
		//~ if >=26.3 'getFrontText()' -> 'getText(net.minecraft.world.level.block.entity.SignTextSlot.FRONT)'
		appendSide(lines, "Front", sign.getFrontText().getMessages(false));
		//~ if >=26.3 'getBackText()' -> 'getText(net.minecraft.world.level.block.entity.SignTextSlot.BACK)'
		appendSide(lines, "Back", sign.getBackText().getMessages(false));
		//~ }

		return lines;
	}

	private void appendSide(List<SignLine> lines, String side, Component[] messages) {
		for (int i = 0; i < messages.length; i++) {
			lines.add(new SignLine(side + " " + (i + 1), messages[i].getString()));
		}
	}

	private String describeMatch(List<SignLine> lines, Pattern pattern) {
		var matched = new ArrayList<String>();

		for (SignLine line : lines) {
			if (pattern.matcher(line.text()).find()) {
				matched.add(line.label() + ": " + line.text());
			}
		}

		return matched.isEmpty() ? null : String.join(" | ", matched);
	}
}
