package tools.redstone.redstonetools.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class PlayerUtils {
	public static Level getWorld(Entity player) {
		return player.level();
	}
}
