package com.domain.redstonetools.features.commands.baseconvert;

import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class BaseConvertOptions extends Options {
    public final Argument<Integer> fromBase = new Argument<>("fromBase", integer(2, 36));
    public final Argument<String> number = new Argument<>("number", word());
    public final Argument<Integer> toBase = new Argument<>("toBase", integer(2, 36));
}
