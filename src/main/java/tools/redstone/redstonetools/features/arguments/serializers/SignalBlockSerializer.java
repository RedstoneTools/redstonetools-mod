package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.context.CommandContext;
import tools.redstone.redstonetools.utils.SignalBlock;

public class SignalBlockSerializer extends EnumSerializer<SignalBlock> {
    private static final SignalBlockSerializer INSTANCE = new SignalBlockSerializer();

    private SignalBlockSerializer() {
        super(SignalBlock.class);
    }

    public static SignalBlockSerializer signalBlock() {
        return INSTANCE;
    }
    public static SignalBlock getSignalBlock(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, SignalBlock.class);
    }
}
