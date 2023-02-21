package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

import java.util.concurrent.atomic.AtomicReference;

public class Argument<T> {
    private String name;
    private final ArgumentType<T> type;
    private boolean optional;
    private final AtomicReference<T> value = new AtomicReference<>(); // use atomic for thread safety
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

    /**
     * Bakes this argument and acquires it's
     * initial value.
     *
     * @return This.
     */
    public Argument<T> build() {
        if (!optional)
            value.set(defaultValue);

        return this;
    }

    /**
     * Update the value of this argument globally
     * based on the given context.
     *
     * @param context The command context.
     */
    @SuppressWarnings("unchecked")
    public void acquireValue(CommandContext<?> context) {
        try {
            if (context == null)
                throw new IllegalArgumentException();
            value.set((T) context.getArgument(name, Object.class));
        } catch (IllegalArgumentException e) {
            if (!optional) {
                throw e;
            }

            value.set(defaultValue);
        }
    }

    public T getValue() {
        return value.get();
    }
}
