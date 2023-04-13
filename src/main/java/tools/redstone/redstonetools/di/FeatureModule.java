package tools.redstone.redstonetools.di;

import tools.redstone.redstonetools.utils.ReflectionUtils;
import com.google.inject.AbstractModule;

public class FeatureModule extends AbstractModule {
    @Override
    protected void configure() {
        for (var featureClass : ReflectionUtils.getFeatureClasses()) {
            bind(featureClass).asEagerSingleton();
        }
    }
}
