package com.domain.redstonetools.features.arguments;

import java.util.Set;

public class BlockColorArgumentType extends SetArgumentType<String> {
    private static final Set<String> COLORS = Set.of(
            "white",
            "orange",
            "magenta",
            "light_blue",
            "yellow",
            "lime",
            "pink",
            "gray",
            "light_gray",
            "cyan",
            "purple",
            "blue",
            "brown",
            "green",
            "red",
            "black"
    );

    private static final BlockColorArgumentType INSTANCE = new BlockColorArgumentType();

    public static BlockColorArgumentType blockColor() {
        return INSTANCE;
    }

    @Override
    protected Set<String> getSet() {
        return COLORS;
    }

    @Override
    protected boolean onlyMatchExact() {
        return false;
    }
}
