package tools.redstone.redstonetools.features.arguments;

import tools.redstone.redstonetools.features.arguments.serializers.EnumSerializer;
import tools.redstone.redstonetools.utils.ColoredBlockTypeArgument;

public class ColoredBlockTypeSerializer extends EnumSerializer<ColoredBlockTypeArgument> {
    private static final ColoredBlockTypeSerializer INSTANCE = new ColoredBlockTypeSerializer();

    private ColoredBlockTypeSerializer() {
        super(ColoredBlockTypeArgument.class);
    }

    public static ColoredBlockTypeSerializer coloredBlockType() {
        return INSTANCE;
    }
}
