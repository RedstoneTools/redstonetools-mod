package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class BrigadierSerializer<T> extends TypeSerializer<T> {
    private final ArgumentType<T> argType;

    protected BrigadierSerializer(Class<T> clazz, ArgumentType<T> argType) {
        super(clazz);
        this.argType = argType;
    }

    @Override
    public final T deserialize(StringReader reader) throws CommandSyntaxException {
        return argType.parse(reader);
    }

    @Override
    public final Collection<String> getExamples() {
        return argType.getExamples();
    }

    @Override
    public final <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return argType.listSuggestions(context, builder);
    }
}
