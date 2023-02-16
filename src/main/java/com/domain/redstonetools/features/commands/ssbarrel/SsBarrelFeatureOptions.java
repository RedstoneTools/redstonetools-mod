package com.domain.redstonetools.features.commands.ssbarrel;

import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;
import com.mojang.brigadier.arguments.IntegerArgumentType;

public class SsBarrelFeatureOptions extends Options {
    public final Argument<Integer> signalStrength = new Argument<>("signalStrength", IntegerArgumentType.integer(0, 15), 15);
}
