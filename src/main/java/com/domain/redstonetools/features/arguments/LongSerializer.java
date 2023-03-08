package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class LongSerializer extends NumberSerializer<Long> {

    static final LongSerializer BASE = new LongSerializer();

    public static LongSerializer longType() {
        return BASE;
    }

    public static LongSerializer longType(long min) {
        return new LongSerializer((double)min, null);
    }

    public static LongSerializer longType(long min, long max) {
        return new LongSerializer((double)min, (double)max);
    }

    //////////////////////////////////////

    LongSerializer() {
        super(Long.class);
    }

    LongSerializer(Double min, Double max) {
        super(Long.class, min, max);
    }

    @Override
    protected Long parse0(StringReader reader) throws CommandSyntaxException {
        return reader.readLong();
    }
}
