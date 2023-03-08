package com.domain.redstonetools.features.arguments;

public abstract class PrimitiveSerializer<T> extends TypeSerializer<T> {

    public PrimitiveSerializer(Class<T> valueType) {
        super(valueType);
    }

    @Override
    public String asString(T value) {
        return String.valueOf(value);
    }

}
