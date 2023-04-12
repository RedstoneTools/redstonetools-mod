package tools.redstone.redstonetools.features.arguments.serializers;

import tools.redstone.redstonetools.macros.MacroManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import tools.redstone.redstonetools.RedstoneToolsClient;

import java.util.concurrent.CompletableFuture;

public class MacroNameSerializer extends StringSerializer {
    private static final MacroNameSerializer INSTANCE = new MacroNameSerializer();

    private MacroNameSerializer() {
        super(greedyString());
    }

    public static MacroNameSerializer macroName() {
        return INSTANCE;
    }

    @Override
    public <R> CompletableFuture<Suggestions> listSuggestions(CommandContext<R> context, SuggestionsBuilder builder) {
        for (var macro : RedstoneToolsClient.INJECTOR.getInstance(MacroManager.class).getMacros()) {
            builder = builder.suggest(serialize(macro.name));
        }

        return builder.buildFuture();
    }
}
