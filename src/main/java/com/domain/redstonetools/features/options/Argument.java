package com.domain.redstonetools.features.options;

import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

import java.util.function.Supplier;

public class Argument<T> {
    public final String name;
    public final ArgumentType<T> type;
    public final boolean optional;
    private final Supplier<T> defaultValue;
    private T value;

    private Argument(String name, ArgumentType<T> type, boolean optional, Supplier<T> defaultValue) {
        this.name = name;
        this.type = type;
        this.optional = optional;
        this.defaultValue = defaultValue;
        if (defaultValue != null)
            this.value = defaultValue.get();
    }

    public Argument(String name, ArgumentType<T> type) {
        this(name, type, false, null);
    }

    public Argument(String name, ArgumentType<T> type, Supplier<T> defaultValue) {
        this(name, type, true, defaultValue);
    }

    public Argument(String name, ArgumentType<T> type, T defaultValue) {
        this(name, type, () -> defaultValue);
    }

    public void updateValue(CommandContext<?> context) {
        try {
            value = context.getArgument(name, ReflectionUtils.getArgumentType(type));
        } catch (IllegalArgumentException e) {
            if (!optional) {
                throw e;
            }

            if (defaultValue != null) {
                value = defaultValue.get();
            }
        }
    }

    public T getValue() {
        return value;
    }
}
