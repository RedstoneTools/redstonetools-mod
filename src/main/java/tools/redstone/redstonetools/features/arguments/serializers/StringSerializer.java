package tools.redstone.redstonetools.features.arguments.serializers;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

@AutoService(GenericArgumentType.class)
public class StringSerializer extends StringBrigadierSerializer<String> {

    private static final StringSerializer INSTANCE = new StringSerializer(StringArgumentType.string());

    public static StringSerializer string() {
        return INSTANCE;
    }

    protected StringSerializer(ArgumentType<String> argumentType) {
        super(String.class, argumentType);
    }

    @Override
    public String serialize(String value) {
        return StringArgumentType.escapeIfRequired(value);
    }
}
