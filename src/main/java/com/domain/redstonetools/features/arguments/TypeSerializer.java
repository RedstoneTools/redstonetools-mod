package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for the 'wrapped' argument type.
 */
public abstract class TypeSerializer<T> implements ArgumentType<T> {

    protected final Class<T> clazz;

    protected TypeSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    /* ArgumentType impl */
    @Override
    public final T parse(StringReader reader) throws CommandSyntaxException {
        return deserialize(reader);
    }

    /* String Serialization */
    public abstract String serialize(T value);
    public abstract T deserialize(StringReader reader) throws CommandSyntaxException;

    /* Command Handling */
    public abstract Collection<String> getExamples();
    public abstract <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder);

    /* Configuration */
    public abstract T load(Object in);
    public abstract Object save(T value);

}
