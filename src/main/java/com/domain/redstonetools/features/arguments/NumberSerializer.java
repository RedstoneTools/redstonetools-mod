package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class NumberSerializer<T extends Number> extends PrimitiveSerializer<T> {

    /* value bound checks */
    Double min, max;

    NumberSerializer(Class<T> tClass) {
        super(tClass);
    }

    NumberSerializer(Class<T> tClass, Double min, Double max) {
        this(tClass);
        this.min = min;
        this.max = max;
    }

    // parse the value from the string reader
    // directly. performs no checks on the value
    protected abstract T parse0(StringReader reader) throws CommandSyntaxException;

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        T res = parse0(reader);
        if (res == null)
            return null;
        if (min != null && res.doubleValue() < min)
            throw new CommandSyntaxException(null,
                    Text.of("Number out of bound; too low (" + res + " < " + min + ")"));
        if (max != null && res.doubleValue() > max)
            throw new CommandSyntaxException(null,
                    Text.of("Number out of bound; too high (" + res + " > " + max + ")"));
        return res;
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("0", "1", "3");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return builder.buildFuture();
    }

}
