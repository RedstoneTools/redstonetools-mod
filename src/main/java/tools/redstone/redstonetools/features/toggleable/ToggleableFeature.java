package tools.redstone.redstonetools.features.toggleable;

import com.sk89q.worldedit.internal.command.CommandUtil;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.commands.CommandFeature;
import tools.redstone.redstonetools.features.feedback.AbstractFeedbackSender;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.features.feedback.FeedbackSender;
import tools.redstone.redstonetools.utils.CommandUtils;
import tools.redstone.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;


import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;
import static net.minecraft.server.command.CommandManager.literal;

public abstract class ToggleableFeature extends AbstractFeature {
    private volatile boolean enabled; // volatile for thread safety
    private Feature info;

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        info = ReflectionUtils.getFeatureInfo(getClass());

        // get arguments and create toggle command
        var arguments = ReflectionUtils.getArguments(getClass());

        var baseCommand = CommandUtils.build(
                info.command(),
                arguments,
                context -> {
                    for (var argument : arguments) {
                        argument.setValue(context);
                    }

                    Feedback.success("Modified properties of feature {}", info.name()).send(context);

                    // enable if disabled
                    if (!enabled) {
                        enable(context);
                    }

                    return 1;
                })
                /* make the base command toggle */
                .executes(this::toggle);

        dispatcher.register(baseCommand);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int toggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return toggle(context.getSource());
    }

    public int toggle(ServerCommandSource source) throws CommandSyntaxException {
        return !enabled ? enable(source) : disable(source);
    }

    //TODO: these need to be replaced when the sendMessage util gets made.
    public int enable(ServerCommandSource source) throws CommandSyntaxException {
        enabled = true;
        Feedback.success("Enabled feature {}", info.name()).send(source);
        return 0;
    }

    public int enable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return enable(context.getSource());
    }

    public int disable(ServerCommandSource source) throws CommandSyntaxException {
        enabled = false;
        Feedback.success("Disabled feature {}", info.name()).send(source);
        return 0;
    }

    public int disable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return disable(context.getSource());
    }

}
