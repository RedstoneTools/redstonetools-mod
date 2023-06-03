package tools.redstone.redstonetools.di;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.macros.MacroManager;
import com.google.inject.AbstractModule;

@AutoService(AbstractModule.class)
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
