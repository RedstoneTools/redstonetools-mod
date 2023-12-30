package tools.redstone.redstonetools.features.arguments.serializers;

import ;
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
        boolean isNegative = false;
        if (serialized.length() == 1) {
            return tryParse(serialized);
        }

        if(serialized.charAt(0) == '-' && serialized.chars().filter(ch -> ch == '-').count() == 1){
            isNegative = true;
            serialized = serialized.replace("-","");
        }

        if (serialized.charAt(0) == '0') {
            if(serialized.length() > 1) {
                var prefixedBase = serialized.substring(0, 2);
                var number = serialized.substring(2);

                var numberBase = NumberBase.fromPrefix(prefixedBase).orElse(null);

                if (numberBase != null) {
                    return isNegative ? tryParse("-" + number, numberBase.toInt()) : tryParse(number, numberBase.toInt());
                }
            } else {
                return tryParse(serialized,10);
            }
        }

        var parts = serialized.split("_", 2);
        if (parts.length == 2) {
            var number = parts[0];

            int base;
            try {
                base = Integer.parseInt(parts[1]);

                if (2 > base || base > 36) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ignored) {
                throw new IllegalArgumentException("Invalid base '" + parts[1] + "'.");
            }

            return isNegative ? tryParse("-"+number, base) : tryParse(number, base);
        }

        return isNegative ? tryParse("-"+serialized) : tryParse(serialized);
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
