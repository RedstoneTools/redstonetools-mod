package com.domain.redstonetools.features.arguments;

import java.util.Set;

public class DirectionArgumentType extends SetArgumentType<String> {
    private static final Set<String> DIRS = Set.of(
            "me",
            "forward",
            "back",
            "north",
            "east",
            "south",
            "west",
            "northeast",
            "northwest",
            "southeast",
            "southwest",
            "up",
            "u",
            "down",
            "d",
            "left",
            "l",
            "right",
            "r"
    );

    private static final DirectionArgumentType INSTANCE = new DirectionArgumentType();

    public static DirectionArgumentType directionArgument() {
        return INSTANCE;
    }

    @Override
    protected Set<String> getSet() {
        return DIRS;
    }

    @Override
    protected boolean onlyMatchExact() {
        return false;
    }
}
