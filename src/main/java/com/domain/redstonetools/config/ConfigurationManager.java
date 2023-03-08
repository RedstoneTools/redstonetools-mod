package com.domain.redstonetools.config;

import com.domain.redstonetools.features.AbstractFeature;

import java.nio.file.Path;

/**
 * The global configuration manager.
 */
public class ConfigurationManager {

    final Path directory = Path.of("./config/redstonetools/");

    public Path getConfigurationFile(AbstractFeature feature) {
        return directory.resolve(feature.getFeatureDescriptor().id() + ".yml");
    }

}
