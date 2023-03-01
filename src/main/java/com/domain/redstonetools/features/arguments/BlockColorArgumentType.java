package com.domain.redstonetools.features.arguments;

import com.domain.redstonetools.utils.ColorUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockColorArgumentType extends SetArgumentType<String> {
    private static final Set<String> COLORS = Arrays.stream(ColorUtils.Color.values())
            .map(x -> x.name).collect(Collectors.toUnmodifiableSet());

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
