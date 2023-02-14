package com.domain.redstonetools.features.commands.baseconvert;

import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class BaseConvertOptions extends Options {
    public final Argument<Integer> fromBase = new Argument<>("fromBase", integer(2, 36));
    public final Argument<Integer> number = new Argument<>("number", integer());
    public final Argument<Integer> toBase = new Argument<Integer>("toBase", integer(2, 36));
}
