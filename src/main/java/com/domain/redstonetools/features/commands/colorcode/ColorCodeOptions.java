package com.domain.redstonetools.features.commands.colorcode;

import com.domain.redstonetools.features.commands.arguments.BlockColorArgumentType;
import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class ColorCodeOptions extends Options {
    public final Argument<String> color = new Argument<>("color", BlockColorArgumentType.blockColor());
    public final Argument<Boolean> onlyWhite = new Argument<>("onlyWhite", BoolArgumentType.bool(), false);
}
