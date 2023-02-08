package com.domain.redstonetools.features;

/**
 * A component of a feature which can
 * be independently initialized.
 */
public interface FeatureComponent extends FeatureAccess {

    void register();

}
