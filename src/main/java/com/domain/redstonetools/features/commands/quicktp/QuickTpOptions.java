package com.domain.redstonetools.features.commands.quicktp;

import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.BoolArgumentType.bool;

public class QuickTpOptions extends Options {
    public final Argument<Float> distance = new Argument<>("distance", floatArg(), 10f);

    public final Argument<Boolean> includeLiquids = new Argument<>("includeLiquids", bool(), false);
}
