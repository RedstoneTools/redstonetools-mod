package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class DoubleSerializer extends NumberSerializer<Double> {

    static final DoubleSerializer BASE = new DoubleSerializer();

    public static DoubleSerializer doubleType() {
        return BASE;
    }

    public static DoubleSerializer doubleType(double min) {
        return new DoubleSerializer(min, null);
    }

    public static DoubleSerializer doubleType(double min, double max) {
        return new DoubleSerializer(min, max);
    }

    //////////////////////////////////////

    DoubleSerializer() {
        super(Double.class);
    }

    DoubleSerializer(Double min, Double max) {
        super(Double.class, min, max);
    }

    @Override
    protected Double parse0(StringReader reader) throws CommandSyntaxException {
        return reader.readDouble();
    }
}
