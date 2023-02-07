package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.FeatureComponent;

public interface TestFeatureComponent extends FeatureComponent {

    @Override
    default void register() {
        System.out.println("[!] TEST from feature " + getClass());
    }

}
