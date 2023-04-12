package tools.redstone.redstonetools.features.arguments.serializers;

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
 *
 * @param <T> The value type.
 * @param <S> The serialized type.
 */
public abstract class TypeSerializer<T, S> implements ArgumentType<T> {

    protected final Class<T> clazz;

    // TODO: Consider moving this constructor to enum serializer as it's the only class that uses the clazz field
    protected TypeSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    /* ArgumentType impl */
    @Override
    public final T parse(StringReader reader) throws CommandSyntaxException {
        return deserialize(reader);
    }

    /* String Serialization */
    public abstract T deserialize(StringReader reader) throws CommandSyntaxException;
    public abstract T deserialize(S serialized);
    public abstract S serialize(T value);

    /* Usage In Commands */
    public abstract Collection<String> getExamples();
    public abstract <R> CompletableFuture<Suggestions> listSuggestions(CommandContext<R> context, SuggestionsBuilder builder);

}
