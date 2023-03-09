package com.domain.redstonetools.config;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.arguments.Argument;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Configuration manager for one feature.
 */
public class FeatureConfiguration {

    final ConfigurationManager manager;

    final AbstractFeature feature;
    final Path file;

    // cached option info
    final List<OptionInfo> options = new ArrayList<>();

    public FeatureConfiguration(
            ConfigurationManager manager,
            AbstractFeature feature
    ) {
        this.manager = manager;
        this.feature = feature;

        this.file = manager.getConfigurationFile(feature);

        // automatically compile
        compile();
    }

    public ConfigurationManager getManager() {
        return manager;
    }

    public AbstractFeature getFeature() {
        return feature;
    }

    public Path getFile() {
        return file;
    }

    public RawConfiguration newRaw() {
        return new RawConfiguration(file, null);
    }

    /**
     * Creates the list of settings from declarations
     * in the feature class using reflection.
     */
    @SuppressWarnings("unchecked")
    public void compile() {
        try {
            Class<?> cl = feature.getClass();
            for (Field field : cl.getDeclaredFields()) {
                field.setAccessible(true);

                // check for argument defaults
                if (Argument.class.isAssignableFrom(field.getType())) {
                    Argument<Object> arg = (Argument<Object>) field.get(feature);
                    if (arg == null)
                        continue;

                    Accessor<Object> accessor = new Accessor<>() {
                        @Override
                        public void set(Object value) {
                            arg.withDefault(value);
                        }

                        @Override
                        public Object get() {
                            return arg.getDefaultValue();
                        }
                    };

                    OptionInfo optionInfo = new OptionInfo(
                            this,
                            "defaults",
                            arg.getName(),
                            arg,
                            accessor
                    );

                    options.add(optionInfo);
                }
            }
        } catch (Exception t) {
            throw new RuntimeException("Error occurred while compiling config for feature " + feature.getIdentifier(), t);
        }
    }

    public void save() {
        try {
            RawConfiguration config = newRaw();

            // put options
            for (OptionInfo optionInfo : options) {
                Section section = config.sectionDeep(optionInfo.section());

                section.set(optionInfo.name(),
                        optionInfo.accessor().get());
            }

            config.save();
        } catch (Exception t) {
            throw new RuntimeException("Error occurred while saving config for feature " + feature.getIdentifier());
        }
    }

    public void load() {
        try {
            if (!Files.exists(file))
                return; // no config to load

            RawConfiguration config = newRaw();
            config.reload();

            // get options
            for (OptionInfo option : options) {
                Section sec = config.sectionDeepOrNull(option.section());
                if (sec == null)
                    continue;

                option.accessor().set(sec.get(option.name()));
            }
        } catch (Exception t) {
            throw new RuntimeException("Error occurred while loading config for feature " + feature.getIdentifier());
        }
    }

}
