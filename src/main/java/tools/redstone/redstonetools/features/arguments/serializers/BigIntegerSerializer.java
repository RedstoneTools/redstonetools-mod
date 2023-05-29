package tools.redstone.redstonetools.features.arguments.serializers;

import java.math.BigInteger;
import java.util.Optional;

public class BigIntegerSerializer extends IntLikeSerializer<BigInteger> {
    private static final BigIntegerSerializer INSTANCE = new BigIntegerSerializer(null, null);

    public static BigIntegerSerializer bigInteger() {
        return INSTANCE;
    }

    public static BigIntegerSerializer bigInteger(BigInteger min) {
        return new BigIntegerSerializer(min, null);
    }

    public static BigIntegerSerializer bigInteger(BigInteger min, BigInteger max) {
        return new BigIntegerSerializer(min, max);
    }

    private BigIntegerSerializer(BigInteger min, BigInteger max) {
        super(BigInteger.class, min, max);
    }

    @Override
    protected Optional<BigInteger> tryParseOptional(String string, int radix) {
        try {
            return Optional.of(new BigInteger(string, radix));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
