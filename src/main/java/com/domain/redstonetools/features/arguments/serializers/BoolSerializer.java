package com.domain.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class BoolSerializer extends StringBrigadierSerializer<Boolean> {

    private static final BoolSerializer INSTANCE = new BoolSerializer(BoolArgumentType.bool());

    public static BoolSerializer bool() {
        return INSTANCE;
    }

    private BoolSerializer(ArgumentType<Boolean> argumentType) {
        super(Boolean.class, argumentType);
    }

    @Override
    public String serialize(Boolean value) {
        return String.valueOf(value);
    }

}
