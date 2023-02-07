package com.domain.redstonetools.features;

import com.domain.redstonetools.features.commands.CommandFeatureComponent;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Represents a feature of the mod.
 */
public abstract class Feature {

    final String name;
    final String displayName;

    public Feature(String name,
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
                lookup.findSpecial(itf, "register", MethodType.methodType(void.class), getClass())
                        .invoke(this);
            } catch (Throwable t) {
                throw new RuntimeException("Failed to initialize component " + itf.getSimpleName(), t);
            }
        }
    }

    /**
     * Get the name of this feature.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the display (proper) name of
     * this feature.
     */
    public String getDisplayName() {
        return displayName;
    }

}
