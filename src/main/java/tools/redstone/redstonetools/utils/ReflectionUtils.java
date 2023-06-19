package tools.redstone.redstonetools.utils;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import rip.hippo.inject.DoctorModule;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private static final Logger LOGGER = LogManager.getLogger();
    private static DoctorModule[] modules;
    private static Set<? extends AbstractFeature> features;

    private ReflectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static DoctorModule[] getModules() {
        if (modules == null) {
            try {
                modules = serviceLoad(DoctorModule.class)
                        .toArray(DoctorModule[]::new);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load modules", e);
            }
        }
        return modules;
    }

    public static Set<? extends AbstractFeature> getFeatures() {
        if (features == null) {
            try {
                features = serviceLoad(AbstractFeature.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load features", e);
            }
        }
        return features;
    }

    private static <T> Set<? extends T> serviceLoad(Class<T> clazz) throws IOException {
        ClassLoader cl = ReflectionUtils.class.getClassLoader();
        Enumeration<URL> serviceFiles = cl.getResources("META-INF/services/" + clazz.getName());
        Set<String> classNames = new HashSet<>();
        while (serviceFiles.hasMoreElements()) {
            URL serviceFile = serviceFiles.nextElement();
            try (var reader = serviceFile.openStream()) {
                classNames.addAll(IOUtils.readLines(reader, "UTF-8"));
            }
        }
        return classNames.stream()
                .filter(it -> !it.isEmpty() && !it.isBlank())
                .map(ReflectionUtils::loadClass)
                .filter(Objects::nonNull)
                .filter(clazz::isAssignableFrom)
                .map(it -> {
                    try {
                        return it.getDeclaredConstructor().newInstance();
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to instantiate " + it, e);
                    }
                })
                .map(clazz::cast)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private static <T> @Nullable Class<? extends T> loadClass(String className) {
        try {
            return (Class<? extends T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load class " + className, e);
        } catch (NoClassDefFoundError e) {
            LOGGER.warn("Failed to load class {}, required {}", className, e.getMessage());
        }
        return null;
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
