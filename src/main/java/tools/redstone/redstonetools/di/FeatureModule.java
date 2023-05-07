package tools.redstone.redstonetools.di;

import com.google.inject.AbstractModule;
import tools.redstone.redstonetools.utils.ReflectionUtils;

public class FeatureModule extends AbstractModule {
    @Override
    protected void configure() {
        for (var featureClass : ReflectionUtils.getFeatureClasses()) {
            bind(featureClass).asEagerSingleton();
        }
    }
}
