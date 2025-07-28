package tools.redstone.redstonetools.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.DirectionArgument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class DirectionArgumentType implements ArgumentType<DirectionArgument> {

    private static final Collection<String> EXAMPLES = Arrays.asList("blue", "red", "magenta");

    public DirectionArgumentType() {
    }
    public static DirectionArgumentType direction() {
        return new DirectionArgumentType();
    }

    public static DirectionArgument getDirection(final CommandContext<?> context, final String name) {
        return context.getArgument(name, DirectionArgument.class);
    }

    @Override
    public DirectionArgument parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final String result = reader.readString();

        DirectionArgument direction =
                switch (result.toUpperCase()) {
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
                    default -> {
                        reader.setCursor(start);
                        throw new SimpleCommandExceptionType(Text.literal("Could not resolve direction!")).create();
                    }
                };
        return direction;
    }

    @Override
    public String toString() {
        return ("directionargument()");
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    String[] directionArguments = {
            "me", "forward", "back", "north", "east", "south", "west",
            "northeast", "northwest", "southeast", "southwest", "up", "down",
            "left", "right"
    };

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(directionArguments, builder);
    }
}
