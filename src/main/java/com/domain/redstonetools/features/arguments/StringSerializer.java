package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import org.apache.commons.lang3.StringEscapeUtils;

public class StringSerializer extends BrigadierSerializer<String> {
    private static final StringSerializer INSTANCE_WORD = new StringSerializer(StringArgumentType.word());
    private static final StringSerializer INSTANCE_STRING = new StringSerializer(StringArgumentType.string());
    private static final StringSerializer INSTANCE_GREEDY_STRING = new StringSerializer(StringArgumentType.greedyString());

    private StringSerializer(StringArgumentType argType) {
        super(String.class, argType);
    }

    public static StringSerializer word() {
        return INSTANCE_WORD;
    }

    public static StringSerializer string() {
        return INSTANCE_STRING;
    }

    public static StringSerializer greedyString() {
        return INSTANCE_GREEDY_STRING;
    }

    @Override
    public String serialize(String value) {
        return StringEscapeUtils.escapeJava(value);
    }
}
