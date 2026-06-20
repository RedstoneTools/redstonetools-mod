package tools.redstone.redstonetools.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class WorldEditUtils {
	public static Region getSelection(ServerPlayer player) throws CommandSyntaxException {
		if (!DependencyLookup.WORLDEDIT_PRESENT) {
			throw new IllegalStateException("WorldEdit is not loaded.");
		}

		//? if <26.1 {
		var actor = FabricAdapter.adaptPlayer(player);
		//? } else
		//var actor = FabricAdapter.get().fromNativePlayer(player);

		var localSession = WorldEdit.getInstance()
				.getSessionManager()
				.get(actor);

		var selectionWorld = localSession.getSelectionWorld();

		try {
			return localSession.getSelection(selectionWorld);
		} catch (IncompleteRegionException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Please make a selection with WorldEdit first")).create();
		}
	}
}
