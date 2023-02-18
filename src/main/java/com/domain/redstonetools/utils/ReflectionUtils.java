package com.domain.redstonetools.utils;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.arguments.TypeProvider;
import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;
import com.mojang.brigadier.arguments.ArgumentType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

// TODO: Someone with a better understanding of Java reflection should probably rewrite this, or maybe we should just use dependency injection
public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static Feature getFeatureInfo(Class<? extends AbstractFeature> featureClass) {
        return featureClass.getAnnotation(Feature.class);
    }

    public static Feature getFeatureInfo(AbstractFeature<?> feature) {
        return getFeatureInfo(feature.getClass());
    }

    public static <O extends Options> O getArgumentInstance(Class<? extends AbstractFeature> featureClass) {
        var optionsClass = getGenericParameters(featureClass)[0];

        try {
            return (O) optionsClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO: Create separate catch blocks for each exception to provide more information
            throw new RuntimeException("Failed to create instance of options class " + optionsClass.getName(), e);
        }
    }

    public static <O extends Options> O getArgumentInstance(AbstractFeature<O> feature) {
        return getArgumentInstance(feature.getClass());
    }

    public static Argument<?>[] getArguments(Object arguments) {
        return Arrays.stream(getArgumentFields(arguments))
                .map(field -> getArgument(arguments, field))
                .toArray(Argument[]::new);
    }

    private static Field[] getArgumentFields(Object options) {
        return options.getClass().getFields();
    }

    public static Argument<?> getArgument(Object arguments, Field option) {
        try {
            return (Argument<?>)option.get(arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] getGenericParameters(Class<?> clazz) {
        // https://stackoverflow.com/questions/1901164/get-type-of-a-generic-parameter-in-java-with-reflection
        return Arrays.stream(((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments())
                .map(type -> (Class<?>)type)
                .toArray(Class[]::new);
    }

    public static AbstractFeature<?> newFeatureInstance(Class<? extends AbstractFeature<?>> featureClass) {
        try {
            return featureClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getArgumentType(Type t) {
        if (t instanceof ParameterizedType pt) {
            Type rt = pt.getRawType();
            if (rt == ArgumentType.class || (rt instanceof Class<?> cl &&
                    TypeProvider.class.isAssignableFrom(cl))) {
                return (Class<?>) pt.getActualTypeArguments()[0];
            }
        }

        if (t instanceof ParameterizedType pt)
            t = pt.getRawType();
        Class<?> klass = (Class<?>) t;
        if (klass == null)
            return null;

        Class<?> r;
        for (Type t1 : klass.getGenericInterfaces()) {
            if ((r = getArgumentType(t1)) != null)
                return r;
        }

        Type sc = klass.getGenericSuperclass();
        if ((r = getArgumentType(sc)) != null)
            return r;

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getArgumentType(ArgumentType<T> type) {
        return (Class<T>) getArgumentType(type.getClass());
    }

}
