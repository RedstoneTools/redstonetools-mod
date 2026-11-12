package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
//? fabric
//import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.*;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.commands.Commands.literal;

public class WorldEditHelperFeature extends ToggleableFeature {
	public static final WorldEditHelperFeature INSTANCE = new WorldEditHelperFeature();

	protected WorldEditHelperFeature() {}

	public void checkPlayers(MinecraftServer server) {
		server.getPlayerList().getPlayers().forEach(this::setPlayerSelection);
	}

	private void setPlayerSelection(ServerPlayer player) {
		Region selection;
		try {
			selection = WorldEditUtils.getSelection(player);
		} catch (CommandSyntaxException ignored) {
			return;
		}
		if (selection == null) {
			hideHelper(player);
			return;
		}

		BlockVector3 pos1 = selection.getBoundingBox().getPos1();
		BlockVector3 pos2 = selection.getBoundingBox().getPos2();

		long volume = selection.getVolume();

		List<Component> lines = new ArrayList<>();

		if (volume != 1L) {
			lines.add(Component.literal("Position 1:").withColor(/*NamedTextColor.DARK_GREEN.value()*/0x00aa00));
			lines.add(Component.literal("   ").append(format(pos1)));

			lines.add(Component.literal("Position 2:").withColor(/*NamedTextColor.DARK_GREEN.value()*/0x00aa00));
			lines.add(Component.literal("   ").append(format(pos2)));
		} else {
			lines.add(Component.literal("Position:").withColor(/*NamedTextColor.DARK_GREEN.value()*/0x00aa00));
			lines.add(Component.literal("   ").append(format(pos1)));
		}

		lines.add(Component.literal("Volume:").withColor(/*NamedTextColor.DARK_GREEN.value()*/0x00aa00));

		int volColor;
		if (volume < 100000) {
			volColor = /*NamedTextColor.GREEN.value()*/0x55ff55;
		} else if (volume < 1000000) {
			volColor = /*NamedTextColor.YELLOW.value()*/0xffff55;
		} else if (volume < 2000000) {
			volColor = /*NamedTextColor.RED.value()*/0xff5555;
		} else {
			volColor = /*NamedTextColor.DARK_RED.value()*/0xaa0000;
		}

		lines.add(Component.literal("   " + volume).withColor(volColor));

		lines.add(Component.literal("Dimensions:").withColor(/*NamedTextColor.DARK_GREEN.value()*/0x00aa00));

		int width = selection.getWidth();
		int height = selection.getHeight();
		int length = selection.getLength();

		//noinspection SuspiciousNameCombination
		Component dimensions = Component.literal("   ")
			.append(formatDimension(width))
			.append(Component.literal("x").withColor(/*NamedTextColor.GRAY.value()*/0xaaaaaa))
			.append(formatDimension(height))
			.append(Component.literal("x").withColor(/*NamedTextColor.GRAY.value()*/0xaaaaaa))
			.append(formatDimension(length));

		lines.add(dimensions);

		Scoreboard scoreboard = new Scoreboard();


		String name = Integer.toString(ThreadLocalRandom.current().nextInt(1234567890));
		scoreboard.addObjective(
			name,
			ObjectiveCriteria.DUMMY,
			Component.literal("Current selection").withColor(/*NamedTextColor.RED.value()*/0xff5555),
			ObjectiveCriteria.RenderType.INTEGER,
			true,
			null
		);

		Objective theObjective = scoreboard.getObjective(name);
		for(net.minecraft.world.scores.DisplaySlot displaySlot : net.minecraft.world.scores.DisplaySlot.values()) {
			if (scoreboard.getDisplayObjective(displaySlot) == theObjective) {
				scoreboard.setDisplayObjective(displaySlot, null);
			}
		}

		DisplaySlot displaySlot = DisplaySlot.SIDEBAR;
		scoreboard.setDisplayObjective(displaySlot, theObjective);

		addLinesToScoreboard(theObjective, lines);
		setScoreboard(player, scoreboard);
	}

	private Component format(BlockVector3 vector) {
		return Component.literal(Integer.toString(vector.x()))
			.withColor(/*NamedTextColor.GRAY.value()*/0xaaaaaa)
			.append(Component.literal(",").withColor(/*NamedTextColor.GRAY.value()*/0xaaaaaa))
			.append(Component.literal(Integer.toString(vector.y()))
				.withColor(/*NamedTextColor.GRAY.value()*/0xaaaaaa))
			.append(Component.literal(",").withColor(/*NamedTextColor.GRAY.value()*/0xaaaaaa))
			.append(Component.literal(Integer.toString(vector.z()))
				.withColor(/*NamedTextColor.GRAY.value()*/0xaaaaaa));
	}

	private Component formatDimension(int x) {
		if (x < 50) {
			return Component.literal(Integer.toString(x)).withColor(/*NamedTextColor.GREEN_VALUE*/0x55ff55);
		} else if (x < 75) {
			return Component.literal(Integer.toString(x)).withColor(/*NamedTextColor.YELLOW_VALUE*/0xffff55);
		} else if (x < 100) {
			return Component.literal(Integer.toString(x)).withColor(/*NamedTextColor.RED_VALUE*/0xff5555);
		} else {
			return Component.literal(Integer.toString(x)).withColor(/*NamedTextColor.DARK_RED_VALUE*/0xaa0000);
		}
	}

	public static void setScoreboard(ServerPlayer player, Scoreboard scoreboard) {
		for (Objective objective : scoreboard.getObjectives()) {
			player.connection.send(
				new ClientboundSetObjectivePacket(
					objective,
					ClientboundSetObjectivePacket.METHOD_ADD
				)
			);
		}

		for (DisplaySlot slot : DisplaySlot.values()) {
			Objective objective = scoreboard.getDisplayObjective(slot);

			if (objective != null) {
				player.connection.send(
					new ClientboundSetDisplayObjectivePacket(slot, objective)
				);
			}
		}

		for (Objective objective : scoreboard.getObjectives()) {
			for (PlayerScoreEntry score : scoreboard.listPlayerScores(objective)) {
				player.connection.send(
					new ClientboundSetScorePacket(
						score.owner(),
						objective.getName(),
						score.value(),
						Optional.ofNullable(score.display()),
						Optional.ofNullable(score.numberFormatOverride())
					)
				);
			}
		}
	}

	private void hideHelper(ServerPlayer player) {
		setScoreboard(player, new Scoreboard());
	}

	private void addLinesToScoreboard(Objective objective, List<Component> lines) {
		List<Component> reversed = new ArrayList<>(lines);
		Collections.reverse(reversed);

		for (int index = 0; index < reversed.size(); index++) {
			Component line = reversed.get(index);

			int finalIndex = index;
			ScoreAccess score = objective.getScoreboard().getOrCreatePlayerScore(() -> ("wehelper-" + finalIndex), objective);
			score.set(index + 1);
			score.display(line);
		}
	}

	// todo: this might be always on..? idk
	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(literal("worldedithelper").requires(tools.redstone.redstonetools.Commands.getPerm("worldedithelper")).executes(this::toggle));
	}

	//? if fabric {
	/*static {
		ServerTickEvents.END_SERVER_TICK.register(WorldEditHelperFeature.INSTANCE::checkPlayers);
		// paper handled in RedstoneToolsListener
	}
	*///? }

	@Override
	public String getName() {
		return "WorldEditHelper";
	}
}
