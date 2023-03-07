package com.domain.redstonetools.features.arguments;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Direction;
import net.minecraft.command.CommandException;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class DirectionArgumentType extends SetArgumentType<String> {
    private static final Set<String> DIRS = Set.of(
            "me",
            "forward",
            "back",
            "north",
            "east",
            "south",
            "west",
            "northeast",
            "northwest",
            "southeast",
            "southwest",
            "up",
            "down",
            "left",
            "right"
    );

    private static final DirectionArgumentType INSTANCE = new DirectionArgumentType();

    public static Direction firstOrdinal(Direction playerFacing) {
        return switch (playerFacing) {
            case EAST_NORTHEAST, NORTH_NORTHEAST -> Direction.NORTHEAST;
            case NORTH_NORTHWEST, WEST_NORTHWEST -> Direction.NORTHWEST;
            case EAST_SOUTHEAST, SOUTH_SOUTHEAST -> Direction.SOUTHEAST;
            case SOUTH_SOUTHWEST, WEST_SOUTHWEST -> Direction.SOUTHWEST;
            default -> playerFacing;
        };
    }

    // big evil match direction function, there might be a better way to do this but i don't know how
    @Nullable
    public static Direction matchDirection(String direction, Direction playerFacing) throws CommandException {
        return switch (direction) {
            case "me", "forward" -> firstOrdinal(playerFacing);
            case "left" -> switch (firstOrdinal(playerFacing)) {
                case UP, DOWN -> throw new CommandException(Text.of("Can't determine direction"));
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
                case NORTHEAST -> Direction.SOUTHEAST;
                case NORTHWEST -> Direction.NORTHEAST;
                case SOUTHWEST -> Direction.NORTHWEST;
                case SOUTHEAST -> Direction.SOUTHWEST;
                default -> null;
            };
            case "right" -> switch (firstOrdinal(playerFacing)) {
                case UP, DOWN -> throw new CommandException(Text.of("Can't determine direction"));
                case NORTH -> Direction.WEST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.EAST;
                case WEST -> Direction.NORTH;
                case NORTHWEST -> Direction.NORTHEAST;
                case NORTHEAST -> Direction.SOUTHEAST;
                case SOUTHEAST -> Direction.SOUTHWEST;
                case SOUTHWEST -> Direction.NORTHWEST;
                default -> null;
            };
            case "back" -> switch (firstOrdinal(playerFacing)) {
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
                case NORTH -> Direction.SOUTH;
                case EAST -> Direction.WEST;
                case SOUTH -> Direction.NORTH;
                case WEST -> Direction.EAST;
                case NORTHWEST -> Direction.SOUTHEAST;
                case NORTHEAST -> Direction.SOUTHWEST;
                case SOUTHEAST -> Direction.NORTHWEST;
                case SOUTHWEST -> Direction.NORTHEAST;
                default -> null;
            };
            case "up" -> Direction.UP;
            case "down" -> Direction.DOWN;
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "east" -> Direction.EAST;
            case "west" -> Direction.WEST;
            case "southeast" -> Direction.SOUTHEAST;
            case "southwest" -> Direction.SOUTHWEST;
            case "northeast" -> Direction.NORTHEAST;
            case "northwest" -> Direction.NORTHWEST;
            default -> null;
        };
    }

    // so many switch cases
    public static BlockVector3 directionToBlock(Direction direction) {
        return switch (direction) {
            case NORTH -> BlockVector3.at(0, 0, -1);
            case EAST -> BlockVector3.at(1, 0, 0);
            case SOUTH -> BlockVector3.at(0, 0, 1);
            case WEST -> BlockVector3.at(-1, 0, 0);
            case UP -> BlockVector3.at(0, 1, 0);
            case DOWN -> BlockVector3.at(0, -1, 0);
            case NORTHEAST -> BlockVector3.at(1, 0, -1);
            case NORTHWEST -> BlockVector3.at(-1, 0, -1);
            case SOUTHEAST -> BlockVector3.at(1, 0, 1);
            case SOUTHWEST -> BlockVector3.at(-1, 0, 1);
            default -> null;
        };
    }

    public static DirectionArgumentType directionArgument() {
        return INSTANCE;
    }

    @Override
    protected Set<String> getSet() {
        return DIRS;
    }

    @Override
    protected boolean onlyMatchExact() {
        return false;
    }
}
