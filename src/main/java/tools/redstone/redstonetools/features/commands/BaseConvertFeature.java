package tools.redstone.redstonetools.features.commands;

import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;

import static tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer.integer;
import static tools.redstone.redstonetools.features.arguments.serializers.NumberBaseSerializer.numberBase;

@Feature(name = "Base Convert", description = "Converts a number from one base to another.", command = "base")
public class BaseConvertFeature extends CommandFeature {

    public static final Argument<Integer> number = Argument
            .ofType(integer());
    public static final Argument<Integer> toBase = Argument
            .ofType(numberBase())
            .withDefault(10);

    @Override
    protected Feedback execute(ServerCommandSource source) {
        var output = Integer.toString(number.getValue(), toBase.getValue());

        return Feedback.success(output);
    }
}
