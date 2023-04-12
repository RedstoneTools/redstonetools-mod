package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;

public class FloatSerializer extends StringBrigadierSerializer<Float> {

    private static final FloatSerializer INSTANCE = new FloatSerializer(FloatArgumentType.floatArg());

    public static FloatSerializer floatArg() {
        return INSTANCE;
    }

    public static FloatSerializer floatArg(float min) {
        return new FloatSerializer(FloatArgumentType.floatArg(min));
    }

    public static FloatSerializer floatArg(float min, float max) {
        return new FloatSerializer(FloatArgumentType.floatArg(min, max));
    }

    private FloatSerializer(ArgumentType<Float> argumentType) {
        super(Float.class, argumentType);
    }

    @Override
    public String serialize(Float value) {
        return String.valueOf(value);
    }

}
