package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class FloatSerializer extends NumberSerializer<Float> {

    static final FloatSerializer BASE = new FloatSerializer();

    public static FloatSerializer floatType() {
        return BASE;
    }

    public static FloatSerializer floatType(float min) {
        return new FloatSerializer((double)min, null);
    }

    public static FloatSerializer floatType(float min, float max) {
        return new FloatSerializer((double)min, (double)max);
    }

    //////////////////////////////////////

    FloatSerializer() {
        super(Float.class);
    }

    FloatSerializer(Double min, Double max) {
        super(Float.class, min, max);
    }

    @Override
    protected Float parse0(StringReader reader) throws CommandSyntaxException {
        return reader.readFloat();
    }
}
