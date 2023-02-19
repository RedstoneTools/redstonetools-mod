package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

public class Argument<T> {
    private String name;
    private final ArgumentType<T> type;
    private boolean optional;
    private T value;
    private T defaultValue;

    private Argument(ArgumentType<T> type) {
        this.type = type;
        optional = false;
    }

    public static <T> Argument<T> ofType(ArgumentType<T> type) {
        return new Argument<>(type);
    }

    public Argument<T> withDefault(T defaultValue) {
        optional = true;
        this.defaultValue = defaultValue;

        return this;
    }

    public Argument<T> named(String name) {
        this.name = name;

        return this;
    }

    public Argument<T> ensureNamed(String fieldName) {
        if (name == null) {
            name = fieldName;
        }

        return this;
    }

    public String getName() {
        return name;
    }

    public ArgumentType<T> getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setValue(CommandContext<?> context) {
        try {
            value = (T) context.getArgument(name, Object.class);
        } catch (IllegalArgumentException e) {
            if (!optional) {
                throw e;
            }

            value = defaultValue;
        }
    }

    public T getValue() {
        return value;
    }
}
