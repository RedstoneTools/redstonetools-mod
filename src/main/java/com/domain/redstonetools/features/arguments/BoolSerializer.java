package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;

public class BoolSerializer extends PrimitiveSerializer<Boolean> {

    static final BoolSerializer BASE = new BoolSerializer();

    public static BoolSerializer bool() {
        return BASE;
    }

    protected BoolSerializer() {
        super(Boolean.class, BoolArgumentType.bool());
    }

}
