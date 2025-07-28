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
import tools.redstone.redstonetools.utils.SignalBlock;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SignalBlockArgumentType implements ArgumentType<SignalBlock> {

    private static final Collection<String> EXAMPLES = Arrays.asList("blue", "red", "magenta");

    public SignalBlockArgumentType() {
    }
    public static SignalBlockArgumentType signalblock() {
        return new SignalBlockArgumentType();
    }

    public static SignalBlock getSignalBlock(final CommandContext<?> context, final String name) {
        return context.getArgument(name, SignalBlock.class);
    }

    @Override
    public SignalBlock parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final String result = reader.readString();

        SignalBlock signalBlock =
                switch (result.toUpperCase()) {
                    case ("BARREL") -> SignalBlock.BARREL;
                    case ("CHEST") -> SignalBlock.CHEST;
                    case ("SHULKER_BOX") -> SignalBlock.SHULKER_BOX;
                    case ("DISPENSER") -> SignalBlock.DISPENSER;
                    case ("DROPPER") -> SignalBlock.DROPPER;
                    case ("HOPPER") -> SignalBlock.HOPPER;
                    case ("BREWING_STAND") -> SignalBlock.BREWING_STAND;
                    case ("FURNACE") -> SignalBlock.FURNACE;
                    case ("SMOKER") -> SignalBlock.SMOKER;
                    case ("BLAST_FURNACE") -> SignalBlock.BLAST_FURNACE;
                    case ("COMMAND_BLOCK") -> SignalBlock.COMMAND_BLOCK;
                    case ("AUTO") -> SignalBlock.AUTO;
                    default -> {
                        reader.setCursor(start);
                        throw new SimpleCommandExceptionType(Text.literal("Could not resolve signal block!")).create();
                    }
                };
        return signalBlock;
    }

    @Override
    public String toString() {
        return ("signalblockargument()");
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    String[] signalBlockArguments = {
            "barrel", "chest", "shulker_box", "dispenser", "dropper", "hopper",
            "brewing_stand", "furnace", "smoker", "blast_furnace", "command_block", "auto"
    };

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(signalBlockArguments, builder);
    }
}
