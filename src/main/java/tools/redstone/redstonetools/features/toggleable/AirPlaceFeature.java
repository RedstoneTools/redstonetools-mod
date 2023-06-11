package tools.redstone.redstonetools.features.toggleable;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.arguments.serializers.BoolSerializer;

import static tools.redstone.redstonetools.features.arguments.serializers.FloatSerializer.floatArg;

@AutoService(AbstractFeature.class)
@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends ToggleableFeature {

    public static final Argument<Float> reach = Argument
            .ofType(floatArg(3.0f))
            .withDefault(5.0f);

    public static final Argument<Boolean> showOutline = Argument
            .ofType(BoolSerializer.bool())
            .withDefault(true);

}
