package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.*;

public class PrimitiveSerializer<T> extends TypeSerializer<T> {

    protected PrimitiveSerializer(Class<T> tClass, ArgumentType<T> mcType) {
        super(tClass, mcType);
    }

    @Override
    public String asString(T value) {
        return String.valueOf(value);
    }

}
