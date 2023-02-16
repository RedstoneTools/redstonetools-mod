package com.domain.redstonetools.utils;

public class RedstoneUtils {
    private RedstoneUtils() {

    }

    public static int getRequiredShovelCount(int signalStrength, int containerSlots) {
        // Formula copied from https://minecraft.fandom.com/wiki/Redstone_Comparator
        return Math.max(signalStrength, (int) Math.ceil((containerSlots / 14.0) * (signalStrength - 1)));
    }
}
