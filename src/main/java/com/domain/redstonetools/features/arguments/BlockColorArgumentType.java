package com.domain.redstonetools.features.arguments;

import com.domain.redstonetools.utils.BlockColor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockColorArgumentType extends SetArgumentType<BlockColor> {
    private static final Set<BlockColor> COLORS = Arrays.stream(BlockColor.values()).collect(Collectors.toUnmodifiableSet());

    private static final BlockColorArgumentType INSTANCE = new BlockColorArgumentType();

    public static BlockColorArgumentType blockColor() {
        return INSTANCE;
    }

    @Override
    protected Set<BlockColor> getSet() {
        return COLORS;
    }

    @Override
    protected boolean onlyMatchExact() {
        return false;
    }
}
