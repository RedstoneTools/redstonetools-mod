package tools.redstone.redstonetools.features.arguments.serializers;

import java.util.Optional;

public class IntegerSerializer extends IntLikeSerializer<Integer> {
    private static final IntegerSerializer INSTANCE = new IntegerSerializer(Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static IntegerSerializer integer() {
        return INSTANCE;
    }

    public static IntegerSerializer integer(int min) {
        return new IntegerSerializer(min, Integer.MAX_VALUE);
    }

    public static IntegerSerializer integer(int min, int max) {
        return new IntegerSerializer(min, max);
    }

    private IntegerSerializer(int min, int max) {
        super(Integer.class, min, max);
    }

    @Override
    protected Optional<Integer> tryParseOptional(String string, int radix) {
        try {
            return Optional.of(Integer.parseInt(string, radix));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
