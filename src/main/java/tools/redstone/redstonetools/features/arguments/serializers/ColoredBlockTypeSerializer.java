package tools.redstone.redstonetools.features.arguments.serializers;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.utils.ColoredBlockType;

@AutoService(GenericArgumentType.class)
public class ColoredBlockTypeSerializer extends EnumSerializer<ColoredBlockType> {
    private static final ColoredBlockTypeSerializer INSTANCE = new ColoredBlockTypeSerializer();

    private ColoredBlockTypeSerializer() {
        super(ColoredBlockType.class);
    }

    public static ColoredBlockTypeSerializer coloredBlockType() {
        return INSTANCE;
    }
}
