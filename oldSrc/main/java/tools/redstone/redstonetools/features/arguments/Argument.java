package tools.redstone.redstonetools.features.arguments;

import tools.redstone.redstonetools.features.arguments.serializers.TypeSerializer;
import com.mojang.brigadier.context.CommandContext;

public class Argument<T> {
    private String name;
    private final TypeSerializer<T, ?> type;
    private boolean optional = false;
    private volatile T value;
    private T defaultValue;

    private Argument(TypeSerializer<T, ?> type) {
        this.type = type;
    }

    public static <T> Argument<T> ofType(TypeSerializer<T, ?> type) {
        return new Argument<>(type);
    }

    public Argument<T> withDefault(T defaultValue) {
        optional = true;
        this.defaultValue = defaultValue;
        this.value = defaultValue; // for options, temporary

        return this;
    }

    public T getDefaultValue() {
        return defaultValue;
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

    public TypeSerializer<T, ?> getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

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

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
