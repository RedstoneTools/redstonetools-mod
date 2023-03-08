package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;

public class FloatSerializer extends BrigadierSerializer<Float> {
    private static final FloatSerializer INSTANCE = new FloatSerializer(FloatArgumentType.floatArg());

    private FloatSerializer(FloatArgumentType argType) {
        super(Float.class, argType);
    }

    public static FloatSerializer floatArg() {
        return INSTANCE;
    }

    public static FloatSerializer floatArg(float min) {
        return new FloatSerializer(FloatArgumentType.floatArg(min));
    }

    public static FloatSerializer floatArg(float min, float max) {
        return new FloatSerializer(FloatArgumentType.floatArg(min, max));
    }

    @Override
    public String serialize(Float value) {
        return value.toString();
    }
}
