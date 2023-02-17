package com.domain.redstonetools.features.options;

import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

public class Argument<T> {
    public final String name;
    public final ArgumentType<T> type;
    public final Class<T> klass;
    public final boolean optional;
    private final T defaultValue;
    private T value;

    private Argument(String name, ArgumentType<T> type, boolean optional, T defaultValue) {
        this.name = name;
        this.type = type;
        this.klass = ReflectionUtils.getArgumentType(type);
        this.optional = optional;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public Argument(String name, ArgumentType<T> type) {
        this(name, type, false, null);
    }

    public Argument(String name, ArgumentType<T> type, T defaultValue) {
        this(name, type, true, defaultValue);
    }

    public void setValue(CommandContext<?> context) {
        try {
            value = context.getArgument(name, klass);
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
