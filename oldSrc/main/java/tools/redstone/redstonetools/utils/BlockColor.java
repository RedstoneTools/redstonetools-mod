package tools.redstone.redstonetools.utils;

import net.minecraft.block.Block;

import java.util.Arrays;

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

    public static BlockColor fromString(String name) {
        return Arrays.stream(BlockColor.values())
                .filter(color -> color.name.equals(name))
                .findFirst()
                .orElseThrow();
    }

    public static BlockColor fromBlock(Block block) {
        var coloredBlock = ColoredBlock.fromBlock(block);

        return coloredBlock == null
                ? BlockColor.WHITE
                : coloredBlock.color;
    }

    @Override
    public String toString() {
        return name;
    }
}
