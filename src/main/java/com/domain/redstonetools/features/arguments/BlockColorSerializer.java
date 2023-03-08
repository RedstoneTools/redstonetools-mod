package com.domain.redstonetools.features.arguments;

import com.domain.redstonetools.utils.BlockColor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockColorSerializer extends OptionSetSerializer<BlockColor> {
    private static final Set<BlockColor> COLORS = Arrays.stream(BlockColor.values()).collect(Collectors.toUnmodifiableSet());

    private static final BlockColorSerializer INSTANCE = new BlockColorSerializer();

    protected BlockColorSerializer() {
        super(BlockColor.class);
    }

    public static BlockColorSerializer blockColor() {
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

    @Override
    public String asString(BlockColor value) {
        return value.toString();
    }
}
