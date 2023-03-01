package com.domain.redstonetools.utils;

import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ColorUtils {
    private static final Pattern MATCH_TARGET_PATH_PATTERN = Pattern.compile(
            "^minecraft:(\\w+?)_(wool|stained_glass|concrete_powder|concrete|glazed_terracotta|terracotta)$"
    );

    // memoize matched block-id's
    private static final HashMap<String, Pair<String, String>> blockMap = new HashMap<>();

    public static Pair<String, String> getMatchedBlockId(String blockId) {
        if (blockMap.containsKey(blockId)) {
            return blockMap.get(blockId);
        }

        if (blockId.equals("minecraft:glass")) {
            blockId = "minecraft:any_stained_glass";
        }

        if (blockId.equals("minecraft:terracotta")) {
            blockId = "minecraft:any_terracotta";
        }

        var match = MATCH_TARGET_PATH_PATTERN.matcher(blockId);

        Pair<String, String> output;
        if (match.matches()) {
            output = new Pair<>(match.group(1), match.group(2));
        } else {
            output = null;
        }

        blockMap.put(blockId, output);

        return output;
    }

    public enum Color {
        WHITE("white"),
        ORANGE("orange"),
        MAGENTA("magenta"),
        LIGHT_BLUE("light_blue"),
        YELLOW("yellow"),
        LIME("lime"),
        PINK("pink"),
        GRAY("gray"),
        LIGHT_GRAY("light_gray"),
        CYAN("cyan"),
        PURPLE("purple"),
        BLUE("blue"),
        BROWN("brown"),
        GREEN("green"),
        RED("red"),
        BLACK("black"),
        ;

        public final String name;

        Color(String name) {
            this.name = name;
        }
    }
}
