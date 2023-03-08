package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;

public class StringSerializer extends WrappingSerializer<String> {

    static final StringSerializer BASE_WORD   = new StringSerializer(StringArgumentType.word());
    static final StringSerializer BASE_STR    = new StringSerializer(StringArgumentType.string());
    static final StringSerializer BASE_GREEDY = new StringSerializer(StringArgumentType.greedyString());

    public static StringSerializer word() {
        return BASE_WORD;
    }

    public static StringSerializer string() {
        return BASE_STR;
    }

    public static StringSerializer greedy() {
        return BASE_GREEDY;
    }

    protected StringSerializer(StringArgumentType type) {
        super(String.class, type);
    }

    @Override
    public String asString(String value) {
        return value;
    }

}
