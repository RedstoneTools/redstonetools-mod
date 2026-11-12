package tools.redstone.redstonetools.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.util.formatting.text.TextComponent;

/**
 * A single search result: a position, and the text displayed for it in a pagination box.
 */
public record LocationContainer(BlockVector3 position, Component match) {

	public static LocationContainer of(BlockVector3 position, String text) {
		return new LocationContainer(position, TextComponent.of(text));
	}

	public static LocationContainer of (BlockVector3 position) {
		return of(position, WorldEditUtils.BV3ToString(position));
	}
}
