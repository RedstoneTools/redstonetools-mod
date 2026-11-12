package tools.redstone.redstonetools.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.UnknownDirectionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class WorldEditUtils {

	/** Beyond this pitch, looking or pointing counts as diagonal rather than flat. */
	private static final float DIAGONAL_PITCH_THRESHOLD = 22.5f;

	public static final long MAX_SCAN_VOLUME = 4_000_000L;

	/**
	 * Adapts a Minecraft server player to a WorldEdit actor.
	 * This is the only platform-specific part of the WorldEdit integration.
	 */
	public static Player getActor(ServerPlayer player) {
		return PlatformAdapter.INSTANCE.adapt(player);
	}

	public static LocalSession getSession(ServerPlayer player) {
		return WorldEdit.getInstance()
			.getSessionManager()
			.get(getActor(player));
	}

	public static Region getSelection(ServerPlayer player) throws CommandSyntaxException {
		var localSession = getSession(player);

		try {
			return localSession.getSelection(localSession.getSelectionWorld());
		} catch (IncompleteRegionException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Please make a selection with WorldEdit first")).create();
		}
	}

	private static ParserContext parserContextFor(ServerPlayer player) {
		var actor = getActor(player);

		var context = new ParserContext();
		context.setActor(actor);
		context.setWorld(actor.getWorld());
		context.setExtent(actor.getWorld());
		context.setSession(getSession(player));
		context.setRestricted(true);

		return context;
	}

	public static Mask parseMask(ServerPlayer player, String input) throws CommandSyntaxException {
		try {
			return WorldEdit.getInstance().getMaskFactory().parseFromInput(input, parserContextFor(player));
		} catch (InputParseException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Invalid mask: " + ex.getMessage())).create();
		}
	}

	public static List<String> suggestMask(ServerPlayer player, String input) {
		return WorldEdit.getInstance().getMaskFactory().getSuggestions(input, parserContextFor(player));
	}

	public static String BV3ToString(BlockVector3 pos) {
		return "(" + pos.x() + ", " + pos.y() + ", " + pos.z() + ")";
	}

	public static void requireScannableVolume(Region region) throws CommandSyntaxException {
		long volume = region.getVolume();

		if (volume > MAX_SCAN_VOLUME) {
			throw new SimpleCommandExceptionType(Component.literal(
				"Selection is too large to scan: " + volume + " blocks, limit is " + MAX_SCAN_VOLUME)).create();
		}
	}

	public static BlockVector3 directionVector(Player actor, String direction) throws UnknownDirectionException {
		WorldEdit worldEdit = WorldEdit.getInstance();

		try {
			// Plain direction, including up and down. Only "me" can be diagonal on this path.
			float pitch = direction.equalsIgnoreCase("me") ? actor.getLocation().getPitch() : 0.0f;
			return withVertical(worldEdit.getDiagonalDirection(actor, direction), pitch);
		} catch (UnknownDirectionException unknown) {
			if (direction.length() < 2) {
				throw unknown;
			}

			char suffix = Character.toLowerCase(direction.charAt(direction.length() - 1));
			if (suffix != 'u' && suffix != 'd') {
				throw unknown;
			}

			BlockVector3 horizontal = worldEdit.getDiagonalDirection(
				actor, direction.substring(0, direction.length() - 1));

			return withVertical(horizontal, suffix == 'u' ? -25.0f : 25.0f);
		}
	}

	private static BlockVector3 withVertical(BlockVector3 vector, float pitch) {
		boolean upright = vector.x() == 0 && vector.z() == 0;

		if (upright || Math.abs(pitch) <= DIAGONAL_PITCH_THRESHOLD) {
			return vector;
		}

		return vector.add(0, pitch < 0 ? 1 : -1, 0);
	}
}
