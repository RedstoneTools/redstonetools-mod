package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * A serializer for single values.
 *
 * These values are always saved in
 * string form, also in configuration.
 */
public abstract class ScalarSerializer<T> extends TypeSerializer<T> {

    protected ScalarSerializer(Class<T> clazz) {
        super(clazz);
    }

    public T load(Object in) {
        try {
            return deserialize(new StringReader(String.valueOf(in)));
        } catch (CommandSyntaxException e) {
            throw new IllegalStateException("Syntax Exception: " + e.getMessage());
        }
    }

    public Object save(T value) {
        return serialize(value);
    }

}
