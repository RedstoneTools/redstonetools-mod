package tools.redstone.redstonetools.utils;

import java.util.Locale;

public enum DirectionArgument {
	ME,
	FORWARD,
	BACK,
	NORTH,
	EAST,
	SOUTH,
	WEST,
	NORTHEAST,
	NORTHWEST,
	SOUTHEAST,
	SOUTHWEST,
	UP,
	DOWN,
	LEFT,
	RIGHT;

	@Override
	public String toString() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}