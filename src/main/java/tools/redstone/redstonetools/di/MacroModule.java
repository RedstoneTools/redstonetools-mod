package tools.redstone.redstonetools.di;

import tools.redstone.redstonetools.macros.MacroManager;
import com.google.inject.AbstractModule;

public class MacroModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MacroManager.class).asEagerSingleton();
    }
}
