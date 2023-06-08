package tools.redstone.redstonetools.features.toggleable;

import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.features.feedback.FeedbackSender;
import tools.redstone.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;


import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;
import static net.minecraft.server.command.CommandManager.literal;

public abstract class ToggleableFeature extends AbstractFeature {
    private boolean enabled;
    private Feature info;

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        info = ReflectionUtils.getFeatureInfo(getClass());

        dispatcher.register(literal(info.command())
                .executes(this::toggle));
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
        INJECTOR.getInstance(FeedbackSender.class).sendFeedback(source, Feedback.success(info.name() + " has been enabled."));
        return 0;
    }

    public int enable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return enable(context.getSource());
    }

    public int disable(ServerCommandSource source) throws CommandSyntaxException {
        enabled = false;
        INJECTOR.getInstance(FeedbackSender.class).sendFeedback(source, Feedback.success(info.name() + " has been disabled."));
        return 0;
    }

    public int disable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return disable(context.getSource());
    }

}
