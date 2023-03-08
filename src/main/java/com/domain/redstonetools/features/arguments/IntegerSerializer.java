package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.IntegerArgumentType;

public class IntegerSerializer extends BrigadierSerializer<Integer> {
    private static final IntegerSerializer INSTANCE = new IntegerSerializer(IntegerArgumentType.integer());

    private IntegerSerializer(IntegerArgumentType argType) {
        super(Integer.class, argType);
    }

    public static IntegerSerializer integer() {
        return INSTANCE;
    }

    public static IntegerSerializer integer(int min) {
        return new IntegerSerializer(IntegerArgumentType.integer(min));
    }

    public static IntegerSerializer integer(int min, int max) {
        return new IntegerSerializer(IntegerArgumentType.integer(min, max));
    }

    @Override
    public String serialize(Integer value) {
        return value.toString();
    }
}
