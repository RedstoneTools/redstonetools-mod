package tools.redstone.redstonetools.utils;

import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FeatureUtils {
    private static Set<AbstractFeature> features;

    private FeatureUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getFeature(Class<T> clazz) {
        if (features == null) {
            Set<AbstractFeature> FEATURES = new HashSet<>();
            FEATURES.add(new AutoDustFeature());
            FEATURES.add(new SignalStrengthBlockFeature());
            FEATURES.add(new QuickTpFeature());
            FEATURES.add(new GiveMeFeature());
            FEATURES.add(new ItemBindFeature());
            FEATURES.add(new ColoredFeature());
            FEATURES.add(new CopyStateFeature());
            if (DependencyLookup.WORLDEDIT_PRESENT) {
                FEATURES.add(new BinaryBlockReadFeature());
                FEATURES.add(new RStackFeature());
                FEATURES.add(new MinSelectionFeature());
                FEATURES.add(new ColorCodeFeature());
            }
            features = FEATURES;
        }
        Optional<T> found = features.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst();
        if (found.isEmpty()) {
            System.out.println("Add " + clazz.getSimpleName() + " to FeatureUtils");
            throw new RuntimeException();
        }
        return found.orElseThrow();
    }
}
