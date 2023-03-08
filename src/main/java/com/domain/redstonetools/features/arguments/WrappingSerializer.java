package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class WrappingSerializer<T> extends TypeSerializer<T> {

    final ArgumentType<T> argumentType;

    public WrappingSerializer(Class<T> valueType, ArgumentType<T> argumentType) {
        super(valueType);
        this.argumentType = argumentType;
    }

    public ArgumentType<T> getArgumentType() {
        return argumentType;
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        return argumentType.parse(reader);
    }

    @Override
    public Collection<String> getExamples() {
        return argumentType.getExamples();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return argumentType.listSuggestions(context, builder);
    }

}
