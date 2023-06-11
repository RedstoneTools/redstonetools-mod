package tools.redstone.redstonetools.features.toggleable;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer;

@AutoService(AbstractFeature.class)
@Feature(name = "Big Dust", description = "Change the size of redstone's hitbox.", command = "bigdust")
public class BigDustFeature extends ToggleableFeature {

    public static final Argument<Integer> heightInPixels = Argument.ofType(IntegerSerializer.integer(1, 16))
            .withDefault(1);


}
