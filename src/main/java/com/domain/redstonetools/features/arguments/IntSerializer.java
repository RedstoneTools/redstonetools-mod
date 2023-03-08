package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class IntSerializer extends NumberSerializer<Integer> {

    static final IntSerializer BASE = new IntSerializer();

    public static IntSerializer integer() {
        return BASE;
    }

    public static IntSerializer integer(int min) {
        return new IntSerializer((double)min, null);
    }

    public static IntSerializer integer(int min, int max) {
        return new IntSerializer((double)min, (double)max);
    }

    //////////////////////////////////////

    IntSerializer() {
        super(Integer.class);
    }

    IntSerializer(Double min, Double max) {
        super(Integer.class, min, max);
    }

    @Override
    protected Integer parse0(StringReader reader) throws CommandSyntaxException {
        return reader.readInt();
    }
}
