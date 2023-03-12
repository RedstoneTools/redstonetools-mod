package com.domain.redstonetools.di;

import com.domain.redstonetools.utils.ReflectionUtils;
import com.google.inject.AbstractModule;

public class FeatureModule extends AbstractModule {
    @Override
    protected void configure() {
        for (var featureClass : ReflectionUtils.getFeatureClasses()) {
            bind(featureClass).asEagerSingleton();
        }
    }
}
