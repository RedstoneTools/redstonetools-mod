package tools.redstone.redstonetools.features.commands;

import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.macros.MacroManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.arguments.serializers.MacroNameSerializer;

@Feature(command = "macro", description = "Allows you to execute a macro", name = "Macro")
public class MacroFeature extends CommandFeature {
    public static final Argument<String> macro = Argument
            .ofType(MacroNameSerializer.macroName());

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        var macroObj = RedstoneToolsClient.INJECTOR.getInstance(MacroManager.class).getMacro(macro.getValue());

        if (macroObj == null) {
            return Feedback.invalidUsage("Macro \"%s\" does not exist".formatted(macro.getValue()));
        }

        macroObj.run();
        return Feedback.none();
    }
}