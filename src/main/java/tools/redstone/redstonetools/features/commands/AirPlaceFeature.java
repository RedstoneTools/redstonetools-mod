package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;

import static tools.redstone.redstonetools.features.arguments.serializers.FloatSerializer.floatArg;

@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends CommandFeature {

    public static final Argument<Float> distance = Argument
            .ofType(floatArg(1.0f))
            .withDefault(null);

    private boolean enabled = false;
    public boolean isEnabled() { return enabled; }

    private float reach = 5.0f;
    public float getReach() { return reach; }
    public void setReach(float reach) { this.reach = reach;}

    private Feedback toggle() {
        enabled = !enabled;
        return enabled ? Feedback.success("AirPlace has been enabled.") : Feedback.success("AirPlace has been disabled.");
    }

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {

        // If no distance is supplied, resort to toggling the feature.
        if ( distance.getValue() == null ) {
            return toggle();
        }

        setReach(distance.getValue());
        return Feedback.success("AirPlace Reach set to " + distance.getValue().toString() + " blocks.");

    }
}
