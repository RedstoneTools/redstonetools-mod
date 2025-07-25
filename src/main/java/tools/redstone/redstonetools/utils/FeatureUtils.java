package tools.redstone.redstonetools.utils;

import com.mojang.brigadier.arguments.ArgumentType;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.commands.update.UpdateFeature;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.macros.MacroManager;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class FeatureUtils {
    private static Set<AbstractFeature> features;

    private FeatureUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getFeature(Class<T> clazz) {
        if (features == null) {
            Set<AbstractFeature> FEATURES = new HashSet<>();
            FEATURES.add(new BigDustFeature());
            FEATURES.add(new AutoDustFeature());
            FEATURES.add(new AirPlaceFeature());
            FEATURES.add(new SignalStrengthBlockFeature());
            FEATURES.add(new RStackFeature());
            FEATURES.add(new QuickTpFeature());
            FEATURES.add(new MinSelectionFeature());
            FEATURES.add(new MacroFeature());
            FEATURES.add(new ItemBindFeature());
            FEATURES.add(new CopyStateFeature());
            FEATURES.add(new ColoredFeature());
            FEATURES.add(new ColorCodeFeature());
            FEATURES.add(new BaseConvertFeature());
            FEATURES.add(new UpdateFeature());
            if (clazz == MacroManager.class) FEATURES.add(new MacroManager());
            features = FEATURES;
        }
        if (clazz == MacroManager.class) features.add(new MacroManager());
        Optional<T> found = features.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst();

        if (found.isEmpty()) {
            System.out.println("Add this one: " + clazz.getName());
            throw new RuntimeException();
        }
        return found.orElseThrow();
    }

    public static List<ArgumentType<?>> getArguments(Class<? extends AbstractFeature> featureClass) {
        return Arrays.stream(featureClass.getFields())
                .filter(field -> ArgumentType.class.isAssignableFrom(field.getType()))
                .map(field -> {
                    if (!Modifier.isPublic(field.getModifiers())
                            || !Modifier.isStatic(field.getModifiers())
                            || !Modifier.isFinal(field.getModifiers())) {
                        throw new RuntimeException("Field " + field.getName() + " of feature " + featureClass.getName() + " is not public static final");
                    }

                    try {
	                    return (ArgumentType<?>) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to get value of field " + field.getName(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    public static Feature getFeatureInfo(Class<? extends AbstractFeature> featureClass) {
        var feature = featureClass.getAnnotation(Feature.class);

        if (feature == null) {
            throw new RuntimeException("No feature annotation found for feature " + featureClass.getName());
        }

        return feature;
    }
}
