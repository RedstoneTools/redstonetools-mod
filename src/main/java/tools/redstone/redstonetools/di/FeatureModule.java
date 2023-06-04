package tools.redstone.redstonetools.di;

import com.google.auto.service.AutoService;
import com.google.inject.AbstractModule;
import tools.redstone.redstonetools.utils.ReflectionUtils;

@AutoService(AbstractModule.class)
public class FeatureModule extends AbstractModule {
    @SuppressWarnings({"rawtypes", "unchecked"}) // this is probably the only way to make it work
    @Override
    protected void configure() {
        for (var feature : ReflectionUtils.getFeatures()) {
            Class clazz = feature.getClass();
            bind(clazz).toInstance(feature);
        }
    }
}
