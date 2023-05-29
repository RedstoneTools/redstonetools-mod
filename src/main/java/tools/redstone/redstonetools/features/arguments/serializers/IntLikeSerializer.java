package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.NumberBase;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class IntLikeSerializer<T extends Comparable<T>> extends TypeSerializer<T, String> {
    private final T min;
    private final boolean hasMin;
    private final T max;
    private final boolean hasMax;

    protected IntLikeSerializer(Class<T> clazz, T min, T max) {
        super(clazz);

        this.min = min;
        hasMin = min != null;
        this.max = max;
        hasMax = max != null;
    }

    @Override
    public T deserialize(StringReader reader) throws CommandSyntaxException {
        var input = reader.readUnquotedString();

        try {
            return deserialize(input);
        } catch (IllegalArgumentException e) {
            throw new CommandSyntaxException(null, Text.of(e.getMessage()));
        }
    }

    @Override
    public T deserialize(String serialized) {
        var value = deserializeUnchecked(serialized);

        if (hasMin && value.compareTo(min) < 0) {
            throw new IllegalArgumentException(value + " is below the minimum of " + min + ".");
        } else if (hasMax && value.compareTo(max) > 0) {
            throw new IllegalArgumentException(value + " is above the maximum of " + max + ".");
        }

        return value;
    }

    private T deserializeUnchecked(String serialized) {
        if (serialized.length() == 1) {
            return tryParse(serialized);
        }

        if (serialized.charAt(0) == '0') {
            var prefixedBase = serialized.substring(0, 2);
            var number = serialized.substring(2);

            // TODO(Refactor): Write a NumberBase.fromCharacter method instead of this that iterates of the NumberBases (add the char to the NumberBase constructor)
            var numberBase = switch (prefixedBase.charAt(1)) {
                case 'b' -> NumberBase.BINARY;
                case 'o' -> NumberBase.OCTAL;
                case 'd' -> NumberBase.DECIMAL;
                case 'x' -> NumberBase.HEXADECIMAL;
                default  -> throw new IllegalArgumentException("Invalid base '" + prefixedBase.charAt(1) + "'.");
            };

            return tryParse(number, numberBase.toInt());
        }

        // TODO(Error handling): Add some checks here to make sure the specified base is valid
        var parts = serialized.split("_", 2);
        if (parts.length == 2) {
            var number = parts[0];
            var base = Integer.parseInt(parts[1]);

            return tryParse(number, base);
        }

        return tryParse(serialized);
    }

    private T tryParse(String string) {
        return tryParse(string, 10);
    }

    private T tryParse(String string, int radix) {
        return tryParseOptional(string, radix)
                .orElseThrow(() -> new IllegalArgumentException(radix == 10
                    ? "Invalid number '" + string + "'."
                    : "Invalid base " + radix + " number '" + string + "'."));
    }

    protected abstract Optional<T> tryParseOptional(String string, int radix);

    @Override
    public String serialize(T value) {
        return String.valueOf(value);
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.emptyList();
    }

    @Override
    public <R> CompletableFuture<Suggestions> listSuggestions(CommandContext<R> context, SuggestionsBuilder builder) {
        return builder.buildFuture();
    }
}
