package tools.redstone.redstonetools.features.arguments.serializers;

import tools.redstone.redstonetools.macros.MacroManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;

public class MacroNameSerializer extends StringSerializer {
    private static final MacroNameSerializer INSTANCE = new MacroNameSerializer();

    private MacroNameSerializer() {
        super(StringSerializer.greedyString());
    }

    public static MacroNameSerializer macroName() {
        return INSTANCE;
    }

    @Override
    public <R> CompletableFuture<Suggestions> listSuggestions(CommandContext<R> context, SuggestionsBuilder builder) {
        for (var macro : INJECTOR.getInstance(MacroManager.class).getMacros()) {
            builder = builder.suggest(serialize(macro.name));
        }

        return builder.buildFuture();
    }
}
