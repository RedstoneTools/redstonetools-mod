package tools.redstone.redstonetools.features;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeature {
    private final Feature featureInfo;

    {
        featureInfo = getClass().getAnnotation(Feature.class);

        if (featureInfo == null) {
            throw new IllegalStateException("Feature " + getClass() + " is not annotated with @Feature");
        }
    }

    public String getName() {
        return featureInfo.name();
    }

    public String getDescription() {
        return featureInfo.description();
    }

    public String getCommand() {
        return featureInfo.command();
    }

    /**
     * Register this feature.
     */
    public void register() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    protected abstract void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);
}
