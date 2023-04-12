package tools.redstone.redstonetools.features.commands;

import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.feedback.AbstractFeedbackSender;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.CommandUtils;
import tools.redstone.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;


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
