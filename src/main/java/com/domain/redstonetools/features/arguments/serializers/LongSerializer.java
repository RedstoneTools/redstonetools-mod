package com.domain.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;

public class LongSerializer extends StringBrigadierSerializer<Long> {

    private static final LongSerializer INSTANCE = new LongSerializer(LongArgumentType.longArg());

    public static LongSerializer longArg() {
        return INSTANCE;
    }

    public static LongSerializer longArg(long min) {
        return new LongSerializer(LongArgumentType.longArg(min));
    }

    public static LongSerializer longArg(long min, long max) {
        return new LongSerializer(LongArgumentType.longArg(min, max));
    }

    private LongSerializer(ArgumentType<Long> argumentType) {
        super(Long.class, argumentType);
    }

    @Override
    public String serialize(Long value) {
        return String.valueOf(value);
    }

}
