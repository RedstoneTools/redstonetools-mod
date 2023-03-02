package com.domain.redstonetools.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;

public class BlockColorUtils {

    private BlockColorUtils(){
    }

    private static final java.util.regex.Pattern MATCH_TARGET_PATH_PATTERN = java.util.regex.Pattern.compile(
            "(_wool$)|(_concrete$)|(_stained_glass$)|(_terracotta$)|(_concrete_powder$)|(_glazed_terracotta$)"
    );

    public static boolean shouldBeColored(World world, BlockVector3 pos, boolean onlyWhite) {
        BlockState state = world.getBlock(pos);
        if (state == null)
            return false;

        var blockColor = getBlockColor(state);
        if (blockColor == null)
            return false;

        if (onlyWhite && !blockColor.equals("white"))  // TODO: Creating an enum for colors would be less error prone
            return false;

        return true;
    }

    public static String getBlockColor(BlockState state) {
        var blockId = state.getBlockType().getId();

        var colorEndIndex = getColorEndIndex(blockId);

        if (colorEndIndex == -1)
            return null;

        return blockId.substring(0, colorEndIndex);
    }

    public static int getColorEndIndex(String blockId) {
        var matcher = MATCH_TARGET_PATH_PATTERN.matcher(blockId);
        if (!matcher.find())
            return -1;

        return matcher.start();
    }

    public static String getColorlessBlockId(BlockState state) {
        var blockId = state.getBlockType().getId();

        var colorEndIndex = BlockColorUtils.getColorEndIndex(blockId);

        if (colorEndIndex == -1)
            return null;

        return blockId.substring(colorEndIndex);
    }

}
