package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * A Brigadier-based serializer which serializes to
 * and deserializes from string.
 *
 * @see BrigadierSerializer
 */
public abstract class StringBrigadierSerializer<T> extends BrigadierSerializer<T, String> {

    public StringBrigadierSerializer(Class<T> clazz, ArgumentType<T> argumentType) {
        super(clazz, argumentType);
    }

    @Override
    public T deserialize(String serialized) {
        try {
            return deserialize(new StringReader(serialized));
        } catch (CommandSyntaxException e) {
            throw new IllegalStateException("Syntax Exception: " + e.getMessage());
        }
    }

}
