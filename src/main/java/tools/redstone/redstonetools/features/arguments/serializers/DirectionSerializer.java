package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.context.CommandContext;
import tools.redstone.redstonetools.utils.DirectionArgument;

public class DirectionSerializer extends EnumSerializer<DirectionArgument> {
    private static final DirectionSerializer INSTANCE = new DirectionSerializer();

    private DirectionSerializer() {
        super(DirectionArgument.class);
    }

    public static DirectionSerializer direction() {
        return INSTANCE;
    }
    public static DirectionArgument getDirection(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, DirectionArgument.class);
    }
}
