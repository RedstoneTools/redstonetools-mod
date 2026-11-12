package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.math.transform.AffineTransform;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.features.logic.RStackArguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class RStackFeature {
	public static final RStackFeature INSTANCE = new RStackFeature();

	protected RStackFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
		var node = dispatcher.register(literal("/rstack")
			.requires(Commands.getPerm("rstack"))
			.executes(context -> execute(context, ""))
			.then(argument("arguments", StringArgumentType.greedyString())
				.executes(context -> execute(context, StringArgumentType.getString(context, "arguments")))));

		dispatcher.register(literal("/rs").redirect(node));
	}

	protected int execute(CommandContext<CommandSourceStack> context, String rawArguments) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var actor = WorldEditUtils.getActor(player);
		var localSession = WorldEditUtils.getSession(player);
		var selection = WorldEditUtils.getSelection(player);

		RStackArguments arguments;
		try {
			arguments = RStackArguments.parse(rawArguments, actor);
		} catch (RStackArguments.ParseException ex) {
			throw new SimpleCommandExceptionType(Component.literal(ex.getMessage())).create();
		}

		BlockVector3 total = arguments.vector().multiply(arguments.count());

		try (var editSession = localSession.createEditSession(actor)) {
			stack(editSession, selection, arguments);

			if (arguments.expand()) {
				selection.expand(total);
			} else if (arguments.shiftSelection()) {
				selection.shift(total);
			}

			localSession.remember(editSession);
		} catch (WorldEditException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Stack failed: " + ex.getMessage())).create();
		}

		if (arguments.expand() || arguments.shiftSelection()) {
			var selector = localSession.getRegionSelector(selection.getWorld());
			selector.learnChanges();
			selector.explainRegionAdjust(actor, localSession);
		}

		context.getSource().sendSystemMessage(
			Component.literal("Stacked %s time(s).".formatted(arguments.count())));
		return 1;
	}

	private void stack(EditSession editSession, Region selection, RStackArguments arguments) throws WorldEditException {
		ForwardExtentCopy copy = new ForwardExtentCopy(
			editSession, selection, editSession, selection.getMinimumPoint());

		copy.setCopyingEntities(false);
		copy.setCopyingBiomes(false);
		copy.setRemovingEntities(false);

		copy.setRepetitions(arguments.count());
		copy.setTransform(new AffineTransform()
			.translate(arguments.vector().x(), arguments.vector().y(), arguments.vector().z()));

		// Without -w, air in the source is skipped, so copies can overlap without erasing.
		if (!arguments.withAir()) {
			copy.setSourceMask(new ExistingBlockMask(selection.getWorld()));
		}

		Operations.complete(copy);
	}
}
