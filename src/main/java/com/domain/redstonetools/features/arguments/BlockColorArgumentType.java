package com.domain.redstonetools.features.arguments;

import com.domain.redstonetools.utils.ColorUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockColorArgumentType extends SetArgumentType<ColorUtils.Color> {
    private static final Set<ColorUtils.Color> COLORS = Arrays.stream(ColorUtils.Color.values()).collect(Collectors.toUnmodifiableSet());

    private static final BlockColorArgumentType INSTANCE = new BlockColorArgumentType();

    public static BlockColorArgumentType blockColor() {
        return INSTANCE;
    }

    @Override
    protected Set<ColorUtils.Color> getSet() {
        return COLORS;
    }

    @Override
    protected boolean onlyMatchExact() {
        return false;
    }
}
