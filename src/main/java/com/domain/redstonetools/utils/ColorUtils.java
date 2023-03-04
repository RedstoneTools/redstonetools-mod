package com.domain.redstonetools.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.domain.redstonetools.RedstoneToolsClient.LOGGER;

public class ColorUtils {
    private static final Pattern MATCH_TARGET_PATH_PATTERN = Pattern.compile(
            "^minecraft:(\\w+?)_(wool|stained_glass|concrete_powder|concrete|glazed_terracotta|terracotta|shulker_box)$"
    );

    // memorize matched block-id's
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

        Pair<String, String> output = null;
        if (match.matches()) {
            output = new Pair<>(match.group(1), match.group(2));
        }

        blockMap.put(blockId, output);

        return output;
    }

    public static boolean shouldBeColored(World world, BlockVector3 pos, ColorUtils.Color onlyColor) {
        var state = world.getBlock(pos);
        var blockId = state.getBlockType().getId();

        var blockPair = getMatchedBlockId(blockId);
        if (blockPair == null) return false;

        if (onlyColor == null) return true;

        var blockColor = blockPair.getA();
        return blockColor.equals("any") || blockColor.equals(onlyColor.toString());
    }

    public static String getBlockColor(BlockState state) {
        String blockId = state.getBlockType().getId();
        Matcher matcher = MATCH_TARGET_PATH_PATTERN.matcher(blockId);

        if (!matcher.matches())
            return null;

        return matcher.group(1);
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

        private final String name;

        Color(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
