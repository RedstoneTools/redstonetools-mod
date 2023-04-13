package tools.redstone.redstonetools.features.toggleable;

import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;

import static tools.redstone.redstonetools.features.arguments.serializers.BoolSerializer.bool;

@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends ToggleableFeature {
    public static final Argument<Boolean> showGhostBlock = Argument
            .ofType(bool())
            .withDefault(true);
}
