package com.domain.redstonetools.config;

import com.domain.redstonetools.features.arguments.Argument;

// caches info about a setting
// compiled from an argument
public record OptionInfo(FeatureConfiguration configuration,
                         String section /* the section this should be under */,
                         String name,
                         Argument<?> argument,
                         Accessor<Object> accessor /* provides get() and set() for values */) {

}
