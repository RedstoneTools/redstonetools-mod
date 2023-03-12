package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.context.CommandContext;

/**
 * An argument to a command or other process
 * as a configuration option.
 *
 * @param <T> The value type.
 */
public class Argument<T> {

    private String name;
    private final TypeSerializer<T, ?> type;
    private boolean optional = false;
    private T value;
    private T defaultValue;
    // TODO: maybe add an isSet flag and unset
    //  the value after executing the command

    private Argument(TypeSerializer<T, ?> type) {
        this.type = type;
    }

    public static <T> Argument<T> ofType(TypeSerializer<T, ?> type) {
        return new Argument<>(type);
    }

    /**
     * Set the default value on this argument.
     * This forces it to be optional.
     *
     * @param defaultValue The value.
     * @return This.
     */
    public Argument<T> withDefault(T defaultValue) {
        optional = true;
        this.defaultValue = defaultValue;

        return this;
    }

    public Argument<T> named(String name) {
        this.name = name;

        return this;
    }

    /**
     * Set the name of this argument to the given
     * value if unset.
     *
     * @param name The name to set.
     * @return This.
     */
    public Argument<T> ensureNamed(String name) {
        if (this.name == null) {
            this.name = name;
        }

        return this;
    }

    /* Getters */

    public String getName() {
        return name;
    }

    public TypeSerializer<T, ?> getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    /**
     * Update the value of this argument using
     * the given command context.
     *
     * @param context The command context.
     */
    @SuppressWarnings("unchecked")
    public void updateValue(CommandContext<?> context) {
        try {
            value = (T) context.getArgument(name, Object.class);
        } catch (IllegalArgumentException e) {
            if (!optional) {
                throw e;
            }

            value = defaultValue;
        }
    }

    /**
     * Get the current value set.
     *
     * @return The value or null if unset.
     */
    public T getValue() {
        return value;
    }

}
