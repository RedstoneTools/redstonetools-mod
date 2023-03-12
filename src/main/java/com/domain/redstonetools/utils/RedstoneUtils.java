package com.domain.redstonetools.utils;

public class RedstoneUtils {
    private RedstoneUtils() {

    }

    /**
     * Convert the target signal strength given to
     * the amount of non-stackable items you would need
     * to put in a container of the given size.
     *
     * @param signalStrength The target signal strength.
     * @param containerSlots The size of the target container.
     * @return The amount of non-stackable items.
     */
    public static int signalStrengthToNonStackableItemCount(int signalStrength, int containerSlots) {
        // Formula copied from https://minecraft.fandom.com/wiki/Redstone_Comparator
        return Math.max(signalStrength, (int) Math.ceil((containerSlots / 14.0) * (signalStrength - 1)));
    }
}
