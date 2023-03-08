package com.domain.redstonetools.features.arguments;

import com.domain.redstonetools.utils.BlockColor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockColorSerializer extends EnumSerializer<BlockColor> {
    private static final BlockColorSerializer INSTANCE = new BlockColorSerializer();

    private BlockColorSerializer() {
        super(BlockColor.class);
    }

    public static BlockColorSerializer blockColor() {
        return INSTANCE;
    }
}
