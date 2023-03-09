package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

public class StringSerializer extends StringBrigadierSerializer<String> {

    private static final StringSerializer INSTANCE_WORD = new StringSerializer(StringArgumentType.word());
    private static final StringSerializer INSTANCE_STRING = new StringSerializer(StringArgumentType.string());
    private static final StringSerializer INSTANCE_GREEDY_STRING = new StringSerializer(StringArgumentType.greedyString());

    public static StringSerializer string() {
        return INSTANCE_STRING;
    }

    public static StringSerializer word() {
        return INSTANCE_WORD;
    }

    public static StringSerializer greedyString() {
        return INSTANCE_GREEDY_STRING;
    }

    private StringSerializer(ArgumentType<String> argumentType) {
        super(String.class, argumentType);
    }

    @Override
    public String serialize(String value) {
        return value;
    }

}
