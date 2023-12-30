package tools.redstone.redstonetools.features.arguments.serializers;

import tools.redstone.redstonetools.utils.DirectionArgument;

public class DirectionSerializer extends EnumSerializer<DirectionArgument> {
    private static final DirectionSerializer INSTANCE = new DirectionSerializer();

    private DirectionSerializer() {
        super(DirectionArgument.class);
    }

    public static DirectionSerializer direction() {
        return INSTANCE;
    }
}
