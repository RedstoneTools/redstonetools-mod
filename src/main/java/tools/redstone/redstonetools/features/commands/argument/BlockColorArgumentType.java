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
import tools.redstone.redstonetools.utils.BlockColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class BlockColorArgumentType implements ArgumentType<BlockColor> {

    private static final Collection<String> EXAMPLES = Arrays.asList("blue", "red", "magenta");

    public BlockColorArgumentType() {
    }
    public static BlockColorArgumentType blockcolor() {
        return new BlockColorArgumentType();
    }

    public static BlockColor getBlockColor(final CommandContext<?> context, final String name) {
        return context.getArgument(name, BlockColor.class);
    }

    @Override
    public BlockColor parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final String result = reader.readString();

        BlockColor color =
            switch (result.toUpperCase()) {
            case ("WHITE") -> BlockColor.WHITE;
            case ("ORANGE") -> BlockColor.ORANGE;
            case ("MAGENTA") -> BlockColor.MAGENTA;
            case ("LIGHT_BLUE") -> BlockColor.LIGHT_BLUE;
            case ("YELLOW") -> BlockColor.YELLOW;
            case ("LIME") -> BlockColor.LIME;
            case ("PINK") -> BlockColor.PINK;
            case ("GRAY") -> BlockColor.GRAY;
            case ("LIGHT_GRAY") -> BlockColor.LIGHT_GRAY;
            case ("CYAN") -> BlockColor.CYAN;
            case ("PURPLE") -> BlockColor.PURPLE;
            case ("BLUE") -> BlockColor.BLUE;
            case ("BROWN") -> BlockColor.BROWN;
            case ("GREEN") -> BlockColor.GREEN;
            case ("RED") -> BlockColor.RED;
            case ("BLACK") -> BlockColor.BLACK;
            default -> {
                reader.setCursor(start);
                throw new SimpleCommandExceptionType(Text.literal("Could not resolve block color!")).create();
                }
            };
        return color;
    }

    @Override
    public String toString() {
        return ("blockcolor()");
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    String[] colors = {
            "white", "orange", "magenta",
            "light_blue", "yellow", "lime",
            "pink", "gray", "light_gray",
            "cyan", "purple", "blue",
            "brown", "green", "red",
            "black"
    };

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(colors, builder);
    }
}
