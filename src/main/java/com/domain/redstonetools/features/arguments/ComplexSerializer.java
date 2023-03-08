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
 * Delegates to a primitive serializer.
 */
public abstract class ComplexSerializer<C, P> extends TypeSerializer<C> {

    final TypeSerializer<P> serializer;

    public ComplexSerializer(Class<C> valueType, TypeSerializer<P> serializer) {
        super(valueType);
        this.serializer = serializer;
    }

    /* Value Conversion Methods */
    protected abstract C toComplex(P src);
    protected abstract P toPrimitive(C src);

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return serializer.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return serializer.getExamples();
    }

    @Override
    public C parse(StringReader reader) throws CommandSyntaxException {
        return toComplex(serializer.parse(reader));
    }

    @Override
    public String asString(C value) {
        return serializer.asString(toPrimitive(value));
    }

}
