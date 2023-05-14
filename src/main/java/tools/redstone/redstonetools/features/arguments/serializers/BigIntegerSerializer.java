package tools.redstone.redstonetools.features.arguments.serializers;

import java.math.BigInteger;

public class BigIntegerSerializer extends IntTypeSerializer<BigInteger> {

    private static BigIntegerSerializer INSTANCE = new BigIntegerSerializer(null, null, 10);

    protected BigIntegerSerializer(BigInteger min, BigInteger max, int defaultBase) {
        super(BigInteger.class, min, max, defaultBase);
    }

    public static BigIntegerSerializer bigInteger() {
        return INSTANCE;
    }

    public static BigIntegerSerializer bigInteger(BigInteger min) {
        return new BigIntegerSerializer(min, null, 10);
    }

    public static BigIntegerSerializer bigInteger(long min) {
        return bigInteger(BigInteger.valueOf(min));
    }

    public static BigIntegerSerializer bigInteger(BigInteger min, BigInteger max) {
        return new BigIntegerSerializer(min, max, 10);
    }

    public static BigIntegerSerializer bigInteger(long min, long max) {
        return bigInteger(BigInteger.valueOf(min), BigInteger.valueOf(max));
    }

    public BigIntegerSerializer withDefaultBase(int base) {
        return new BigIntegerSerializer(min, max, base);
    }

    @Override
    public BigInteger parse(String str, int radix) {
        return new BigInteger(str, radix);
    }

    @Override
    public String stringify(BigInteger value, int radix) {
        return value.toString(radix);
    }

    @Override
    public boolean isGreaterThan(BigInteger value, BigInteger other) {
        return value.compareTo(other) > 0;
    }

    @Override
    public boolean isLessThan(BigInteger value, BigInteger other) {
        return value.compareTo(other) < 0;
    }

}
