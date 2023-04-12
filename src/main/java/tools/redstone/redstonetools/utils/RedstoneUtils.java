package tools.redstone.redstonetools.utils;

public class RedstoneUtils {
    private RedstoneUtils() {

    }

    public static int signalStrengthToNonStackableItemCount(int signalStrength, int containerSlots) {
        // Formula copied from https://minecraft.fandom.com/wiki/Redstone_Comparator
        return Math.max(signalStrength, (int) Math.ceil((containerSlots / 14.0) * (signalStrength - 1)));
    }
}
