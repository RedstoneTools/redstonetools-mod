package tools.redstone.redstonetools;

import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.commands.update.UpdateFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.utils.DependencyLookup;

public class Commands {
    public static void registerCommands() {
        if (DependencyLookup.WORLDEDIT_PRESENT) {
            UpdateFeature.registerCommand();
            BinaryBlockReadFeature.registerCommand();
            ColorCodeFeature.registerCommand();
            MinSelectionFeature.registerCommand();
            RStackFeature.registerCommand();
        }
        QuickTpFeature.registerCommand();
        SignalStrengthBlockFeature.registerCommand();
        GiveMeFeature.registerCommand();
        BigDustFeature.registerCommand();
        AutoDustFeature.registerCommand();
    }
}
