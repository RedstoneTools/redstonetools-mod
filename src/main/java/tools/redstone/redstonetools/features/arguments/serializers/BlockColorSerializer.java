package tools.redstone.redstonetools.features.arguments.serializers;

import tools.redstone.redstonetools.utils.BlockColor;

public class BlockColorSerializer extends EnumSerializer<BlockColor> {
    private static final BlockColorSerializer INSTANCE = new BlockColorSerializer();

    private BlockColorSerializer() {
        super(BlockColor.class);
    }

    public static BlockColorSerializer blockColor() {
        return INSTANCE;
    }
}
