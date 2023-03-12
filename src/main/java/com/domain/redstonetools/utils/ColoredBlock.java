package com.domain.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Represents a color-able block type with an associated {@link BlockColor},
 * which can be recolored.
 */
public class ColoredBlock {

    /**
     * The regex to recognize colored blocks
     * and capture their color and colorless ID.
     */
    private static final Pattern COLORED_BLOCK_REGEX = Pattern.compile(
            "^minecraft:(\\w+?)_(wool|stained_glass|concrete_powder|concrete|glazed_terracotta|terracotta)$"
    );

    private static final HashMap<String, ColoredBlock> COLORLESS_BLOCKS = new HashMap<>() {{
        put("minecraft:glass", new ColoredBlock("minecraft:%s_stained_glass", BlockColor.WHITE));
        put("minecraft:terracotta", new ColoredBlock("minecraft:%s_terracotta", BlockColor.WHITE));
    }};

    /** Cached for performance. */
    private static final HashMap<String, ColoredBlock> COLORED_BLOCK_CACHE = new HashMap<>() {{
        putAll(COLORLESS_BLOCKS);
    }};

    private final String blockIdFormat;
    public final BlockColor color;

    public ColoredBlock(String blockIdFormat, BlockColor color) {
        this.blockIdFormat = blockIdFormat;
        this.color = color;
    }

    public ColoredBlock withColor(BlockColor color) {
        return new ColoredBlock(blockIdFormat, color);
    }

    /**
     * Parse the colored block from the given block
     * identifier.
     *
     * @param blockId The block identifier.
     * @return The colored block.
     */
    public static ColoredBlock fromBlockId(String blockId) {
        if (COLORED_BLOCK_CACHE.containsKey(blockId)) {
            return COLORED_BLOCK_CACHE.get(blockId);
        }

        var matcher = COLORED_BLOCK_REGEX.matcher(blockId);
        if (!matcher.matches()) {
            return null;
        }

        var color = matcher.group(1);
        var blockType = matcher.group(2);

        var coloredBlock = new ColoredBlock("minecraft:%s_" + blockType, BlockColor.fromString(color));

        COLORED_BLOCK_CACHE.put(blockId, coloredBlock);

        return coloredBlock;
    }

    /**
     * Format the block ID with the color
     * of this block.
     *
     * @return The identifier.
     */
    public String toBlockId() {
        return String.format(blockIdFormat, color);
    }

    public static ColoredBlock fromBlock(@NotNull Block block) {
        var blockId = Registry.BLOCK.getId(block).toString();
        if (COLORED_BLOCK_CACHE.containsKey(blockId)) {
            return COLORED_BLOCK_CACHE.get(blockId);
        }

        var coloredBlock = fromBlockId(blockId);

        // The reason we only cache nulls here and not in the fromBlockId method is because the fromBlockId method would
        // also cache invalid block ids (literally any string) which would make the cache massive. This method however
        // only accepts actual blocks which means that the cache will never grow bigger than the amount of mc blocks.
        COLORED_BLOCK_CACHE.put(blockId, coloredBlock);

        return coloredBlock;
    }

    public Block toBlock() {
        return Registry.BLOCK.get(Identifier.tryParse(toBlockId()));
    }

    @Override
    public String toString() {
        return toBlockId();
    }

}
