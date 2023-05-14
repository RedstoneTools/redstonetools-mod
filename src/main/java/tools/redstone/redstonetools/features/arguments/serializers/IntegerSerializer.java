package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

public class IntegerSerializer extends IntTypeSerializer<Integer> {

    private static final IntegerSerializer INSTANCE = new IntegerSerializer(null, null, 10);

    public IntegerSerializer(Integer min, Integer max, int defaultBase) {
        super(Integer.class, min, max, defaultBase);
    }

    public static IntegerSerializer integer() {
        return INSTANCE;
    }

    public static IntegerSerializer integer(int min) {
        return new IntegerSerializer(min, null, 10);
    }

    public static IntegerSerializer integer(int min, int max) {
        return new IntegerSerializer(min, max, 10);
    }

    public IntegerSerializer withDefaultBase(int base) {
        return new IntegerSerializer(min, max, base);
    }

    @Override
    public Integer parse(String str, int radix) {
        return Integer.parseInt(str, radix);
    }

    @Override
    public String stringify(Integer value, int radix) {
        return Integer.toString(value, radix);
    }

    @Override
    public boolean isGreaterThan(Integer value, Integer other) {
        return value > other;
    }

    @Override
    public boolean isLessThan(Integer value, Integer other) {
        return value < other;
    }

}
