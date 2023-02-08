package com.domain.redstonetools.features;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a feature of the mod.
 */
public abstract class AbstractFeature implements FeatureAccess {

    final String name;
    final String displayName;

    final Map<Class<?>, Object> services = new HashMap<>();

    public AbstractFeature(String name,
                           String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    /**
     * Registers this feature.
     */
    public void register() {
        // initialize components
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        for (Class<?> itf : getClass().getInterfaces()) {
            if (!FeatureComponent.class.isAssignableFrom(itf))
                continue;

            try {
                lookup.findSpecial(
                        itf, "register",
                        MethodType.methodType(void.class), getClass()
                )
                        .invoke(this);
            } catch (Throwable t) {
                throw new RuntimeException("Failed to initialize component " + itf.getSimpleName(), t);
            }
        }
    }

    public void withService(Class<?> klass, Object o) {
        services.put(klass, o);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> tClass) {
        return (T) services.get(tClass);
    }

    /**
     * Get the name of this feature.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the display (proper) name of
     * this feature.
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

}
