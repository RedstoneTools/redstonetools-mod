package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;

import java.math.BigInteger;

import static tools.redstone.redstonetools.features.arguments.serializers.BigIntegerSerializer.bigInteger;
import static tools.redstone.redstonetools.features.arguments.serializers.NumberBaseSerializer.numberBase;

@AutoService(AbstractFeature.class)
@Feature(name = "Base Convert", description = "Converts a number from one base to another.", command = "base")
public class BaseConvertFeature extends CommandFeature {

    public static final Argument<BigInteger> number = Argument
            .ofType(bigInteger());
    public static final Argument<Integer> toBase = Argument
            .ofType(numberBase())
            .withDefault(10);

    @Override
    protected Feedback execute(ServerCommandSource source) {
        var output = number.getValue().toString(toBase.getValue());

        return Feedback.success("%s", new String[] { output });
    }
}
