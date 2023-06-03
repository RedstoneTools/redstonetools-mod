package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;

import static tools.redstone.redstonetools.features.arguments.serializers.FloatSerializer.floatArg;

@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends CommandFeature {

    public boolean enabled;
    public float reach = 5.0f;

    public static final Argument<Float> distance = Argument
        .ofType(floatArg(1.0f))
        .withDefault(null);

    private void toggle() {
        enabled = !enabled;
    }

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {

        // If no distance is supplied, resort to toggling the feature.
        if ( distance.getValue() == null ) {
            toggle();
            return enabled ? Feedback.success("AirPlace has been enabled.") : Feedback.success("AirPlace has been disabled.");
        }

        reach = distance.getValue();
        return Feedback.success("AirPlace Reach set to " + distance.getValue().toString() + " blocks.");

    }
}
