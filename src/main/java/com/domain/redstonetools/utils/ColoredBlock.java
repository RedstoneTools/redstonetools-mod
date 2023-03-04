package com.domain.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ColoredBlock {
    private static final Pattern COLORED_BLOCK_REGEX = Pattern.compile(
            "^minecraft:(\\w+?)_(wool|stained_glass|concrete_powder|concrete|glazed_terracotta|terracotta|shulker_box)$"
    );

    private static final HashMap<String, ColoredBlock> coloredBlockCache = new HashMap<>()
    {
        {
            put("minecraft:glass", new ColoredBlock("minecraft:%s_stained_glass", BlockColor.WHITE));
            put("minecraft:terracotta", new ColoredBlock("minecraft:%s_terracotta", BlockColor.WHITE));
        }
    };

    private final String blockIdFormat;
    public final BlockColor color;

    public ColoredBlock(String blockIdFormat, BlockColor color) {
        this.blockIdFormat = blockIdFormat;
        this.color = color;
    }

    public ColoredBlock withColor(BlockColor color) {
        return new ColoredBlock(blockIdFormat, color);
    }

    public static ColoredBlock fromBlockId(String blockId) {
        if (coloredBlockCache.containsKey(blockId)) {
            return coloredBlockCache.get(blockId);
        }

        var matcher = COLORED_BLOCK_REGEX.matcher(blockId);
        if (!matcher.matches()) {
            return null;
        }

        var color = matcher.group(1);
        var blockType = matcher.group(2);

        var coloredBlock = new ColoredBlock("minecraft:%s_" + blockType, BlockColor.fromString(color));

        coloredBlockCache.put(blockId, coloredBlock);

        return coloredBlock;
    }

    public String toBlockId() {
        return String.format(blockIdFormat, color);
    }

    public static ColoredBlock fromBlock(@NotNull Block block) {
        return fromBlockId(Registry.BLOCK.getId(block).toString());
    }

    public Block toBlock() {
        return Registry.BLOCK.get(Identifier.tryParse(toBlockId()));
    }

    @Override
    public String toString() {
        return toBlockId();
    }
}
