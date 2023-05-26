package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.NumberBase;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class IntegerSerializer extends TypeSerializer<Integer, String> {

    private static final IntegerSerializer INSTANCE = new IntegerSerializer();

    private final int min;
    private final int max;

    public static IntegerSerializer integer() {
        return INSTANCE;
    }

    public static IntegerSerializer integer(int min) {
        return new IntegerSerializer(min);
    }

    public static IntegerSerializer integer(int min, int max) {
        return new IntegerSerializer(min, max);
    }

    private IntegerSerializer(int min, int max) {
        super(Integer.class);

        this.min = min;
        this.max = max;
    }

    private IntegerSerializer(int min) {
        this(min, Integer.MAX_VALUE);
    }

    private IntegerSerializer() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public Integer deserialize(StringReader reader) throws CommandSyntaxException {
        var input = reader.readUnquotedString();

        try {
            return deserialize(input);
        } catch (IllegalArgumentException e) {
            throw new CommandSyntaxException(null, Text.of(e.getMessage()));
        }
    }

    @Override
    public Integer deserialize(String serialized) {
        var value = deserializeUnchecked(serialized);

        if (value < min) {
            throw new IllegalArgumentException(value + " is below the minimum of " + min + ".");
        } else if (value > max) {
            throw new IllegalArgumentException(value + " is above the maximum of " + max + ".");
        }

        return value;
    }

    private Integer deserializeUnchecked(String serialized) {
        if (serialized.length() == 1) {
            return tryParseInteger(serialized);
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

            return tryParseInteger(number, numberBase.toInt());
        }

        // TODO(Error handling): Add some checks here to make sure the specified base is valid
        var parts = serialized.split("_", 2);
        if (parts.length == 2) {
            var number = parts[0];
            var base = Integer.parseInt(parts[1]);

            return tryParseInteger(number, base);
        }

        return tryParseInteger(serialized);
    }

    private Integer tryParseInteger(String string) {
        return tryParseInteger(string, 10);
    }

    private Integer tryParseInteger(String string, int radix) {
        try {
            return Integer.parseInt(string, radix);
        } catch (NumberFormatException ignored) {
            if (radix != 10) {
                throw new IllegalArgumentException("Invalid base " + radix + " number '" + string + "'.");
            }

            throw new IllegalArgumentException("Invalid number '" + string + "'.");
        }
    }

    @Override
    public String serialize(Integer value) {
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
