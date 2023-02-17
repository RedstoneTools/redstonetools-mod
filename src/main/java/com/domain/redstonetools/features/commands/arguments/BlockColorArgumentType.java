package com.domain.redstonetools.features.commands.arguments;

import java.util.List;

public class BlockColorArgumentType extends SetArgumentType<String> {

    static final List<String> COLORS = List.of(
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

    static final BlockColorArgumentType INSTANCE = new BlockColorArgumentType();

    public static BlockColorArgumentType blockColor() {
        return INSTANCE;
    }

    @Override
    protected List<String> getSet() {
        return COLORS;
    }

    @Override
    protected boolean isOnlyExact() {
        return false;
    }

}
