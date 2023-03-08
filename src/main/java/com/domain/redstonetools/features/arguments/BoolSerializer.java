package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BoolSerializer extends PrimitiveSerializer<Boolean> {

    static final BoolSerializer BASE = new BoolSerializer();

    public static BoolSerializer bool() {
        return BASE;
    }

    protected BoolSerializer() {
        super(Boolean.class);
    }

    @Override
    public Boolean parse(StringReader reader) throws CommandSyntaxException {
        String word = reader.readUnquotedString();
        return switch (word) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new CommandSyntaxException(null, Text.of(word + " is not a valid boolean"));
        };
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        builder.suggest("true");
        builder.suggest("false");
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("true", "false");
    }

}
