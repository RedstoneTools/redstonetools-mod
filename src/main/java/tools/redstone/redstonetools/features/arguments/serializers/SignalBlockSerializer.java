package tools.redstone.redstonetools.features.arguments.serializers;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.utils.SignalBlock;

@AutoService(GenericArgumentType.class)
public class SignalBlockSerializer extends EnumSerializer<SignalBlock> {
    private static final SignalBlockSerializer INSTANCE = new SignalBlockSerializer();

    private SignalBlockSerializer() {
        super(SignalBlock.class);
    }

    public static SignalBlockSerializer signalBlock() {
        return INSTANCE;
    }
}
