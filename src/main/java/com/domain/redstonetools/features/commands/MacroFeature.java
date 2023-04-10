package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.domain.redstonetools.features.feedback.Feedback;
import com.domain.redstonetools.macros.MacroManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import static com.domain.redstonetools.RedstoneToolsClient.INJECTOR;
import static com.domain.redstonetools.features.arguments.serializers.MacroNameSerializer.macroName;

@Feature(command = "macro", description = "Allows you to execute a macro", name = "Macro")
public class MacroFeature extends CommandFeature {
    public static final Argument<String> macro = Argument
            .ofType(macroName());

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        var macroObj = INJECTOR.getInstance(MacroManager.class).getMacro(macro.getValue());

        if (macroObj == null) {
            return Feedback.invalidUsage("Macro \"%s\" does not exist".formatted(macro.getValue()));
        }

        macroObj.run();
        return Feedback.none();
    }
}
