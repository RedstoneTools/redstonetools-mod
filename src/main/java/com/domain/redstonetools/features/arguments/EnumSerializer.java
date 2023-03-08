package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class EnumSerializer<T extends Enum<T>> extends TypeSerializer<T> {

    /**
     * Get if this serializer should only
     * match exact options.
     *
     * @return True/false.
     */
    protected abstract boolean onlyMatchExact();

    protected EnumSerializer(Class<T> tClass) {
        super(tClass);
    }

    public T find(String input) throws CommandSyntaxException {
        var matches = Arrays.stream(valueType.getEnumConstants())
                .filter(s -> onlyMatchExact() ?
                        s.toString().equalsIgnoreCase(input) :
                        s.toString().toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            throw new CommandSyntaxException(null, Text.of("No such option '" + input + "'"));
        }

        if (matches.size() > 1) {
            throw new CommandSyntaxException(null, Text.of("Ambiguous option '" + input + "'"));
        }

        return matches.get(0);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        return find(reader.readString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (var option : valueType.getEnumConstants()) {
            builder.suggest(option.toString());
        }

        return builder.buildFuture();
    }

    @Override
    public String asString(T value) {
        return value.toString();
    }

}
