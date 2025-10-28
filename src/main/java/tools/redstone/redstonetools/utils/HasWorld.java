package tools.redstone.redstonetools.utils;

import net.minecraft.world.World;

public interface HasWorld {
	default World getWorld() {
		// this needs to have a body for loom injected interfaces to work, it can't be abstract
		throw new IllegalStateException();
	}
}
