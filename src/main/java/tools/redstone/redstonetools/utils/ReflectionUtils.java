package tools.redstone.redstonetools.utils;

import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import com.google.inject.AbstractModule;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private ReflectionUtils() { }

    private static final Reflections reflections = new Reflections("com.domain.redstonetools");

    public static Set<? extends AbstractModule> getModules() {
        return getModuleClasses().stream()
                .map(clazz -> {
                    try {
                        return clazz.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException("Failed to instantiate module " + clazz.getName(), e);
                    }
                })
                .collect(Collectors.toSet());
    }

    private static Set<Class<? extends AbstractModule>> getModuleClasses() {
        return getConcreteSubclasses(AbstractModule.class);
    }

    public static Set<Class<? extends AbstractFeature>> getFeatureClasses() {
        return getConcreteSubclasses(AbstractFeature.class);
    }

    private static <T> Set<Class<? extends T>> getConcreteSubclasses(Class<T> clazz) {
        return reflections.getSubTypesOf(clazz).stream()
                .filter(subclass -> !Modifier.isAbstract(subclass.getModifiers()))
                .collect(Collectors.toSet());
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
