package com.domain.redstonetools.config;

import com.domain.redstonetools.features.AbstractFeature;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * The global configuration manager.
 */
public class ConfigurationManager {

    static final ConfigurationManager INSTANCE =
            new ConfigurationManager();

    public static ConfigurationManager get() {
        return INSTANCE;
    }

    /////////////////////////////////////////

    final Path directory = Path.of("./config/redstonetools/");
    final Map<String, FeatureConfiguration> configs = new HashMap<>();

    {
        try {
            if (!Files.exists(directory))
                Files.createDirectories(directory);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to create config directory " + directory);
        }
    }

    public Path getConfigurationFile(AbstractFeature feature) {
        return directory.resolve(feature.getIdentifier() + ".yml");
    }

    public FeatureConfiguration getConfiguration(String id) {
        return configs.get(id);
    }

    public FeatureConfiguration newConfiguration(AbstractFeature feature) {
        FeatureConfiguration config = new FeatureConfiguration(this, feature);
        configs.put(feature.getIdentifier(), config);
        return config;
    }

}
