package com.domain.redstonetools.features;

public interface FeatureAccess {

    <T> T getService(Class<T> tClass);

    /**
     * Get the name of this feature.
     */
    String getName();

    /**
     * Get the display (proper) name of
     * this feature.
     */
    String getDisplayName();

}
