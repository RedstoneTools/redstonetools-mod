package tools.redstone.redstonetools.features.arguments;

import tools.redstone.redstonetools.features.arguments.serializers.EnumSerializer;
import tools.redstone.redstonetools.utils.ColoredBlockArgument;

public class ColorSwitchSerializer extends EnumSerializer<ColoredBlockArgument> {
    private static final ColorSwitchSerializer INSTANCE = new ColorSwitchSerializer();

    private ColorSwitchSerializer() {
        super(ColoredBlockArgument.class);
    }

    public static ColorSwitchSerializer material() {
        return INSTANCE;
    }
}
