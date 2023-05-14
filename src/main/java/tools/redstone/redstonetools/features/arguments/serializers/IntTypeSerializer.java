package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A type serializer for all integer number types, such as
 * int, long and {@link java.math.BigInteger}.
 * @param <T>
 */
public abstract class IntTypeSerializer<T extends Number> extends TypeSerializer<T, String> {

    private static final byte[] CHAR_TO_BASE = new byte[256];
    private static final char[] BASE_TO_CHAR = new char[36];

    static {
        CHAR_TO_BASE['b'] = 2;
        CHAR_TO_BASE['o'] = 8;
        CHAR_TO_BASE['d'] = 10;
        CHAR_TO_BASE['x'] = 16;

        BASE_TO_CHAR[2]  = 'b';
        BASE_TO_CHAR[8]  = 'o';
        BASE_TO_CHAR[10] = 'd';
        BASE_TO_CHAR[16] = 'x';
    }

    protected final T min;
    protected final T max;

    // the standard base to read in and
    // output to
    protected final int defaultBase;

    protected IntTypeSerializer(Class<T> clazz, T min, T max, int defaultBase) {
        super(clazz);
        this.min = min;
        this.max = max;
        this.defaultBase = defaultBase;
    }

    public abstract T parse(String str, int radix);
    public abstract String stringify(T value, int radix);
    public abstract boolean isGreaterThan(T value, T other);
    public abstract boolean isLessThan(T value, T other);

    @Override
    public String serialize(T value) {
        return "0" + BASE_TO_CHAR[defaultBase] + stringify(value, defaultBase);
    }

    @Override
    public T deserialize(String serialized) {
        int i = 0;
        int radix = defaultBase;

        // check for alternate radix
        if (serialized.length() > 2) {
            if (serialized.charAt(0) == '0') {
                char rc = serialized.charAt(1);
                int rt = CHAR_TO_BASE[rc];
                if (rt != 0) {
                    radix = rt;
                    i += 2;
                }
            }
        }

        // parse number
        T num = parse(serialized.substring(i), radix);

        // check bounds
        if (min != null && isLessThan(num, min))
            throw new NumberFormatException("value too small, value: " + num + ", minimum: " + min);
        if (max != null && isGreaterThan(num, max))
            throw new NumberFormatException("value too large, value: " + num + ", maximum: " + min);
        return num;
    }

    @Override
    public T deserialize(StringReader reader) throws CommandSyntaxException {
        try {
            return deserialize(reader.readUnquotedString());
        } catch (NumberFormatException e) {
            throw new CommandSyntaxException(null, Text.of("Invalid number input: " + e.getMessage()));
        }
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("0d12345", "0b1001", "0xABC");
    }

    @Override
    public <R> CompletableFuture<Suggestions> listSuggestions(CommandContext<R> context, SuggestionsBuilder builder) {
        builder.suggest("0b");
        builder.suggest("0o");
        builder.suggest("0d");
        builder.suggest("0x");

        for (int i = 0; i < 10; i++) {
            builder.suggest(builder.getInput() + i);
        }

        return builder.buildFuture();
    }

}
