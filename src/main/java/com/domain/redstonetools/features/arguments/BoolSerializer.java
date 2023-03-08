package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;

public class BoolSerializer extends BrigadierSerializer<Boolean> {
    private static final BoolSerializer INSTANCE = new BoolSerializer();

    private BoolSerializer() {
        super(Boolean.class, BoolArgumentType.bool());
    }

    public static BoolSerializer bool() {
        return INSTANCE;
    }

    @Override
    public String serialize(Boolean value) {
        return value.toString();
    }
}
