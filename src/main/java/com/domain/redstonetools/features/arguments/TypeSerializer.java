package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for the 'wrapped' argument type.
 */
public abstract class TypeSerializer<T> implements ArgumentType<T> {

    protected final Class<T> valueType;

    @Nullable
    protected final ArgumentType<T> wrappedType;

    public TypeSerializer(Class<T> valueType, @Nullable ArgumentType<T> wrappedType) {
        this.valueType = valueType;
        this.wrappedType = wrappedType;
    }

    public TypeSerializer(Class<T> valueType) {
        this(valueType, null);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        if (wrappedType == null)
            throw new UnsupportedOperationException();
        return wrappedType.parse(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (wrappedType == null)
            throw new UnsupportedOperationException();
        return wrappedType.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        if (wrappedType == null)
            throw new UnsupportedOperationException();
        return wrappedType.getExamples();
    }

    public Class<T> getValueType() {
        return valueType;
    }

    /**
     * Stringify the given value.
     *
     * @param value The value.
     * @return The string.
     */
    public abstract String asString(T value);

}
