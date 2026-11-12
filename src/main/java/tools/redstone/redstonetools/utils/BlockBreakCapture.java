package tools.redstone.redstonetools.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Lets a feature claim the next block a player breaks. The break is cancelled and handed
 * to the handler instead. Fed by the platform's block break hook.
 */
public class BlockBreakCapture {
	private static final Map<UUID, BiConsumer<ServerPlayer, BlockPos>> HANDLERS = new HashMap<>();

	/** @return false when the player is already waiting to pick a block. */
	public static boolean claimNextBreak(ServerPlayer player, BiConsumer<ServerPlayer, BlockPos> handler) {
		return HANDLERS.putIfAbsent(player.getUUID(), handler) == null;
	}

	public static void release(UUID uuid) {
		HANDLERS.remove(uuid);
	}

	/** @return true when the break was consumed and must be cancelled. */
	public static boolean consume(ServerPlayer player, BlockPos pos) {
		BiConsumer<ServerPlayer, BlockPos> handler = HANDLERS.remove(player.getUUID());

		if (handler == null) {
			return false;
		}

		handler.accept(player, pos);
		return true;
	}
}