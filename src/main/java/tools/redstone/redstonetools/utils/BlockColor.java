package tools.redstone.redstonetools.utils;

import java.util.Arrays;

/**
 * A Minecraft block color, on for example
 * wool or concrete.
 */
public enum BlockColor {

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
    BLACK("black");

    private final String name;

    BlockColor(String name) {
        this.name = name;
    }

    /**
     * Get a block color by name.
     */
    public static BlockColor fromString(String name) {
        return Arrays.stream(BlockColor.values())
                .filter(color -> color.name.equals(name))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public String toString() {
        return name;
    }

}
