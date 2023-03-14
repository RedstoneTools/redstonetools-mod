package com.domain.redstonetools.features.arguments;

import com.domain.redstonetools.utils.DirectionArgument;

public class DirectionSerializer extends EnumSerializer<DirectionArgument> {
    private static final DirectionSerializer INSTANCE = new DirectionSerializer();

    private DirectionSerializer() {
        super(DirectionArgument.class);
    }

    public static DirectionSerializer directionArgument() {
        return INSTANCE;
    }
}
