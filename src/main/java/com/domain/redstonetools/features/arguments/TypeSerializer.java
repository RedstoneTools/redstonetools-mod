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

    public TypeSerializer(Class<T> valueType) {
        this.valueType = valueType;
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
