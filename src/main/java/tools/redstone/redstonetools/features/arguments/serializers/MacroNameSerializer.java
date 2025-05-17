package tools.redstone.redstonetools.features.arguments.serializers;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import tools.redstone.redstonetools.macros.MacroManager;

import java.util.concurrent.CompletableFuture;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;

@AutoService(GenericArgumentType.class)
public class MacroNameSerializer extends StringSerializer {
    private static final MacroNameSerializer INSTANCE = new MacroNameSerializer();

    private MacroNameSerializer() {

        super(StringSerializer.string());

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
