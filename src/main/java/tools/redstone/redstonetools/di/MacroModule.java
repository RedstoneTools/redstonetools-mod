package tools.redstone.redstonetools.di;

import tools.redstone.redstonetools.macros.MacroManager;
import com.google.inject.AbstractModule;

public class MacroModule extends AbstractModule {

    private static MacroManager macroManager;

    @Override
    protected void configure() {
        bind(MacroManager.class).toProvider(() -> {
            if (macroManager == null) {
                macroManager = new MacroManager();
            }

            return macroManager;
        });

    }
}
