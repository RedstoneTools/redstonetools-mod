package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

public class StringSerializer extends StringBrigadierSerializer<String> {

    static final StringSerializer INSTANCE_WORD = new StringSerializer(StringArgumentType.word());
    static final StringSerializer INSTANCE_STRING = new StringSerializer(StringArgumentType.string());
    static final StringSerializer INSTANCE_GREEDY = new StringSerializer(StringArgumentType.greedyString());

    public static StringSerializer string() {
        return INSTANCE_STRING;
    }

    public static StringSerializer word() {
        return INSTANCE_WORD;
    }

    public static StringSerializer greedy() {
        return INSTANCE_GREEDY;
    }

    public StringSerializer(ArgumentType<String> argumentType) {
        super(String.class, argumentType);
    }

    @Override
    public String serialize(String value) {
        return value;
    }

}
