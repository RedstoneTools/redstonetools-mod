package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.feedback.AbstractFeedbackSender;
import com.domain.redstonetools.feedback.Feedback;
import com.domain.redstonetools.utils.CommandUtils;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import static com.domain.redstonetools.RedstoneToolsClient.INJECTOR;

public abstract class CommandFeature extends AbstractFeature {
    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        var info = ReflectionUtils.getFeatureInfo(getClass());
        var arguments = ReflectionUtils.getArguments(getClass());

        CommandUtils.register(
                info.command(),
                arguments,
                context -> {
                    for (var argument : arguments) {
                        argument.setValue(context);
                    }

                    var feedback = execute(context.getSource());

                    INJECTOR.getInstance(AbstractFeedbackSender.class)
                            .sendFeedback(context.getSource(), feedback);

                    return feedback.getType().getCode();
                },
                dispatcher,
                dedicated);
    }

    protected abstract Feedback execute(ServerCommandSource source) throws CommandSyntaxException;
}
