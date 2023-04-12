package tools.redstone.redstonetools.features.commands;

import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer;
import tools.redstone.redstonetools.features.arguments.serializers.StringSerializer;

import static tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer.integer;

import java.math.BigInteger;

@Feature(name = "Base Convert", description = "Converts a number from one base to another.", command = "base")
public class BaseConvertFeature extends CommandFeature {
    public static final Argument<Integer> fromBase = Argument
            .ofType(IntegerSerializer.integer(2, 36));
    public static final Argument<String> number = Argument
            .ofType(StringSerializer.word());
    public static final Argument<Integer> toBase = Argument
            .ofType(IntegerSerializer.integer(2, 36));

    @Override
    protected Feedback execute(ServerCommandSource source) {
        BigInteger input;
        try {
            input = new BigInteger(number.getValue(), fromBase.getValue());
        } catch (NumberFormatException e) {
            return Feedback.invalidUsage("Inputted number does not match the specified base");
        }

        var output = input.toString(toBase.getValue());
        return Feedback.success(output);
    }

}
