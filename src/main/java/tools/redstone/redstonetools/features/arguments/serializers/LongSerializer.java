package tools.redstone.redstonetools.features.arguments.serializers;

import java.util.Optional;

public class LongSerializer extends IntLikeArgumentType<Long> {
    private static final LongSerializer INSTANCE = new LongSerializer(Long.MIN_VALUE, Long.MAX_VALUE);

    public static LongSerializer longArg() {
        return INSTANCE;
    }

    public static LongSerializer longArg(long min) {
        return new LongSerializer(min, Long.MAX_VALUE);
    }

    public static LongSerializer longArg(long min, long max) {
        return new LongSerializer(min, max);
    }

    private LongSerializer(long min, long max) {
        super(Long.class, min, max);
    }

    @Override
    protected Optional<Long> tryParseOptional(String string, int radix) {
        try {
            return Optional.of(Long.parseLong(string, radix));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
