package com.domain.redstonetools.features.arguments;

import com.domain.redstonetools.utils.BlockColor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockColorSerializer extends EnumSerializer<BlockColor> {

    static final BlockColorSerializer BASE_NOT_EXACT = new BlockColorSerializer(false);
    static final BlockColorSerializer BASE_EXACT     = new BlockColorSerializer(true);

    public static BlockColorSerializer blockColor() {
        return BASE_NOT_EXACT;
    }

    public static BlockColorSerializer blockColorExact() {
        return BASE_EXACT;
    }

    final boolean onlyMatchExact;

    protected BlockColorSerializer(boolean onlyMatchExact) {
        super(BlockColor.class);
        this.onlyMatchExact = onlyMatchExact;
    }

    @Override
    protected boolean onlyMatchExact() {
        return onlyMatchExact;
    }

}
