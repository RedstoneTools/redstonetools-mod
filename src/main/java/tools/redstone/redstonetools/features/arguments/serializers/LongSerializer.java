package tools.redstone.redstonetools.features.arguments.serializers;

public class LongSerializer extends IntTypeSerializer<Long> {

    private static final LongSerializer INSTANCE = new LongSerializer(null, null, 10);

    public LongSerializer(Long min, Long max, int defaultBase) {
        super(Long.class, min, max, defaultBase);
    }

    public static LongSerializer longArg() {
        return INSTANCE;
    }

    public static LongSerializer longArg(long min) {
        return new LongSerializer(min, null, 10);
    }

    public static LongSerializer longArg(long min, long max) {
        return new LongSerializer(min, max, 10);
    }

    public LongSerializer withDefaultBase(int base) {
        return new LongSerializer(min, max, base);
    }

    @Override
    public Long parse(String str, int radix) {
        return Long.parseLong(str, radix);
    }

    @Override
    public String stringify(Long value, int radix) {
        return Long.toString(value, radix);
    }

    @Override
    public boolean isGreaterThan(Long value, Long other) {
        return value > other;
    }

    @Override
    public boolean isLessThan(Long value, Long other) {
        return value < other;
    }

}
