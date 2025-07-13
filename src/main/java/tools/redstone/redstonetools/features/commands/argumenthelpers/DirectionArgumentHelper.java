package tools.redstone.redstonetools.features.commands.argumenthelpers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.DirectionArgument;

public class DirectionArgumentHelper {
	public static DirectionArgument getDirection(CommandContext<ServerCommandSource> context, String direction) throws CommandSyntaxException {
		String directionAsString = context.getArgument(direction, String.class);
		return switch (directionAsString.toUpperCase()) {
			case ("ME") -> DirectionArgument.ME;
			case ("FORWARD") -> DirectionArgument.FORWARD;
			case ("BACK") -> DirectionArgument.BACK;
			case ("NORTH") -> DirectionArgument.NORTH;
			case ("EAST") -> DirectionArgument.EAST;
			case ("SOUTH") -> DirectionArgument.SOUTH;
			case ("WEST") -> DirectionArgument.WEST;
			case ("NORTHEAST") -> DirectionArgument.NORTHEAST;
			case ("NORTHWEST") -> DirectionArgument.NORTHWEST;
			case ("SOUTHEAST") -> DirectionArgument.SOUTHEAST;
			case ("SOUTHWEST") -> DirectionArgument.SOUTHWEST;
			case ("UP") -> DirectionArgument.UP;
			case ("DOWN") -> DirectionArgument.DOWN;
			case ("LEFT") -> DirectionArgument.LEFT;
			case ("RIGHT") -> DirectionArgument.RIGHT;
			default -> throw new SimpleCommandExceptionType(Text.literal("Could not resolve direction!")).create();
		};
	}
}
