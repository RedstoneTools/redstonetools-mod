package com.domain.redstonetools.utils;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.google.inject.AbstractModule;
import org.reflections.Reflections;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private ReflectionUtils() { }

    private static final Reflections reflections = new Reflections("com.domain.redstonetools");
    private static final MethodHandles.Lookup INTERNAL_LOOKUP;
    private static final Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new ExceptionInInitializerError(t);
        }

        try {
            // get lookup
            Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            MethodHandles.publicLookup();
            INTERNAL_LOOKUP = (MethodHandles.Lookup)
                    unsafe.getObject(
                            unsafe.staticFieldBase(field),
                            unsafe.staticFieldOffset(field)
                    );
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MethodHandles.Lookup getInternalLookup() {
        return INTERNAL_LOOKUP;
    }

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
