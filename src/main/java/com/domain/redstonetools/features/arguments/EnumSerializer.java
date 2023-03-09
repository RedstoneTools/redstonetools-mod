package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public abstract class EnumSerializer<T extends Enum<T>> extends ScalarSerializer<T> {
    protected EnumSerializer(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public String serialize(T value) {
        return value.toString();
    }

    @Override
    public T deserialize(StringReader reader) throws CommandSyntaxException {
        var input = reader.readUnquotedString();
        var inputLowerCase = input.toLowerCase();

        var matches = EnumSet.allOf(clazz).stream()
                .filter(elem -> serialize(elem).toLowerCase().startsWith(inputLowerCase))
                .toList();

        if (matches.isEmpty()) {
            throw new CommandSyntaxException(null, Text.of("No such option '" + input + "'"));
        }

        if (matches.size() > 1) {
            throw new CommandSyntaxException(null, Text.of("Ambiguous option '" + input + "'"));
        }

        return matches.get(0);
    }

    @Override
    public Collection<String> getExamples() {
        return EnumSet.allOf(clazz).stream()
                .map(Enum::toString)
                .toList();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (var value : EnumSet.allOf(clazz)) {
            builder = builder.suggest(value.toString());
        }

        return builder.buildFuture();
    }
}
