package tools.redstone.redstonetools.di;

import tools.redstone.redstonetools.utils.ReflectionUtils;
import com.google.inject.AbstractModule;
import tools.redstone.redstonetools.utils.WorldEditUtils;

public class FeatureModule extends AbstractModule {
    @Override
    protected void configure() {
        for (var featureClass : ReflectionUtils.getFeatureClasses()) {
            if (ReflectionUtils.getFeatureInfo(featureClass).isWorldEditFeature() && !WorldEditUtils.isInstalled()) {
                return;
            }

            bind(featureClass).asEagerSingleton();
        }
    }
}
