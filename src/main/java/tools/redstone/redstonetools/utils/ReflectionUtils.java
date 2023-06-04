package tools.redstone.redstonetools.utils;

import rip.hippo.inject.DoctorModule;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private static final ServiceLoader<DoctorModule> modulesLoader =
            ServiceLoader.load(DoctorModule.class);
    private static final ServiceLoader<AbstractFeature> featuresLoader =
            ServiceLoader.load(AbstractFeature.class);
    private static DoctorModule[] modules;
    private static Set<? extends AbstractFeature> features;

    private ReflectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static DoctorModule[] getModules() {
        if (modules == null) {
            modules = modulesLoader.stream()
                    .map(ServiceLoader.Provider::get)
                    .toArray(DoctorModule[]::new);
        }
        return modules;
    }

    public static Set<? extends AbstractFeature> getFeatures() {
        if (features == null) {
            features = featuresLoader.stream()
                    .map(ServiceLoader.Provider::get)
                    .collect(Collectors.toSet());
        }
        return features;
    }

    public static List<Argument<?>> getArguments(Class<? extends AbstractFeature> featureClass) {
        return Arrays.stream(featureClass.getFields())
                .filter(field -> Argument.class.isAssignableFrom(field.getType()))
                .map(field -> {
                    if (!Modifier.isPublic(field.getModifiers())
                            || !Modifier.isStatic(field.getModifiers())
                            || !Modifier.isFinal(field.getModifiers())) {
                        throw new RuntimeException("Field " + field.getName() + " of feature " + featureClass.getName() + " is not public static final");
                    }

                    try {
                        var argument = (Argument<?>) field.get(null);

                        return argument.ensureNamed(field.getName());
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
