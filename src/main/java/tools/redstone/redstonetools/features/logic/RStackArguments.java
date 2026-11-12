package tools.redstone.redstonetools.features.logic;

import com.sk89q.worldedit.UnknownDirectionException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.math.BlockVector3;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.Objects;

public record RStackArguments(int count, BlockVector3 vector, boolean expand, boolean withAir, boolean shiftSelection) {
	public static final int DEFAULT_COUNT = 1;
	public static final int DEFAULT_SPACING = 2;

	public static class ParseException extends Exception {
		public ParseException(String message) {
			super(message);
		}
	}

	public static RStackArguments parse(String input, Player actor) throws ParseException {
		Integer count = null;
		Integer spacing = null;
		BlockVector3 directionVector = null;
		BlockVector3 explicitVector = null;
		boolean expand = false;
		boolean withAir = false;
		boolean shiftSelection = false;

		for (String token : input.trim().split("\\s+")) {
			if (isFlags(token)) {
				for (char flag : token.substring(1).toCharArray()) {
					switch (flag) {
						case 'e' -> expand = true;
						case 'w' -> withAir = true;
						case 's' -> shiftSelection = true;
						default -> throw new ParseException("Unknown flag: -" + flag);
					}
				}
				continue;
			}

			if (token.indexOf(',') >= 0) {
				if (explicitVector != null) {
					throw new ParseException("Only one vector can be given");
				}
				explicitVector = parseVector(token);
				continue;
			}

			Integer number = tryParseInt(token);
			if (number != null) {
				if (count == null) {
					count = number;
				} else if (spacing == null) {
					spacing = number;
				} else {
					throw new ParseException("Too many numbers, expected a count and a spacing");
				}
				continue;
			}

			BlockVector3 asDirection;
			try {
				asDirection = WorldEditUtils.directionVector(actor, token);
			} catch (UnknownDirectionException ex) {
				throw new ParseException("Unknown argument: " + token);
			}

			if (asDirection == null) {
				throw new ParseException("Unknown argument: " + token);
			}
			if (directionVector != null) {
				throw new ParseException("Only one direction can be given");
			}
			directionVector = asDirection;
		}

		if (explicitVector != null && (directionVector != null || spacing != null)) {
			throw new ParseException("Give either a vector, or a direction and a spacing, not both");
		}

		BlockVector3 vector;
		if (explicitVector != null) {
			vector = explicitVector;
		} else {
			if (directionVector == null) {
				try {
					directionVector = WorldEditUtils.directionVector(actor, "me");
				} catch (UnknownDirectionException ex) {
					throw new ParseException(ex.getMessage());
				}
			}
			vector = Objects.requireNonNull(directionVector).multiply(spacing == null ? DEFAULT_SPACING : spacing);
		}

		return new RStackArguments(count == null ? DEFAULT_COUNT : count, vector, expand, withAir, shiftSelection);
	}

	private static boolean isFlags(String token) {
		return token.length() >= 2 && token.charAt(0) == '-' && Character.isLetter(token.charAt(1));
	}

	private static BlockVector3 parseVector(String token) throws ParseException {
		String[] parts = token.split(",", -1);
		if (parts.length != 3) {
			throw new ParseException("A vector is written x,y,z");
		}

		Integer x = tryParseInt(parts[0]);
		Integer y = tryParseInt(parts[1]);
		Integer z = tryParseInt(parts[2]);
		if (x == null || y == null || z == null) {
			throw new ParseException("A vector is written x,y,z with whole numbers");
		}

		return BlockVector3.at(x, y, z);
	}

	private static Integer tryParseInt(String token) {
		try {
			return Integer.valueOf(token);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}