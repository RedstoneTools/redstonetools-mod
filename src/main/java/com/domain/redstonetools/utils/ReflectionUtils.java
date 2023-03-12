package com.domain.redstonetools.utils;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
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

    /**
     * Gets all module classes using {@link #getModuleClasses()}
     * and instantiates them.
     *
     * @return The module set.
     */
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

    /**
     * Get all classes which extend {@link AbstractModule}.
     *
     * @return The module classes.
     */
    private static Set<Class<? extends AbstractModule>> getModuleClasses() {
        return getConcreteSubclasses(AbstractModule.class);
    }

    /**
     * Get all classes which extend {@link AbstractFeature}.
     *
     * @return The feature classes.
     */
    public static Set<Class<? extends AbstractFeature>> getFeatureClasses() {
        return getConcreteSubclasses(AbstractFeature.class);
    }

    private static <T> Set<Class<? extends T>> getConcreteSubclasses(Class<T> clazz) {
        return reflections.getSubTypesOf(clazz).stream()
                .filter(subclass -> !Modifier.isAbstract(subclass.getModifiers()))
                .collect(Collectors.toSet());
    }

    /**
     * Get and load all declared arguments in the
     * given feature class.
     *
     * @param featureClass The feature class to search.
     * @return The list of arguments.
     */
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

    /**
     * Get the feature descriptor of the given
     * feature class if present.
     *
     * @param featureClass The feature class.
     * @throws RuntimeException If no feature annotation is present.
     * @return The annotation instance.
     */
    public static Feature getFeatureInfo(Class<? extends AbstractFeature> featureClass) {
        var feature = featureClass.getAnnotation(Feature.class);

        if (feature == null) {
            throw new RuntimeException("No feature annotation found for feature " + featureClass.getName());
        }

        return feature;
    }
}
