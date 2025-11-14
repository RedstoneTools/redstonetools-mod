package tools.redstone.redstonetools.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class PlayerUtils {
	public static World getWorld(Entity player) {
		//? if <1.21.10 {
		/*return player.getWorld();
		 *///?} else {
		return player.getEntityWorld();
		//?}
	}
}
