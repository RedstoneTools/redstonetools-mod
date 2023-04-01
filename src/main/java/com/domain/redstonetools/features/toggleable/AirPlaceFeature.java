package com.domain.redstonetools.features.toggleable;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;

import static com.domain.redstonetools.features.arguments.serializers.BoolSerializer.bool;

@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends ToggleableFeature {
    public static final Argument<Boolean> showGhostBlock = Argument
            .ofType(bool())
            .withDefault(true);
}
