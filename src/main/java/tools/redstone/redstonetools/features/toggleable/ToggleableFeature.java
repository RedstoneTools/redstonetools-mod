package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.arguments.serializers.BoolSerializer;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.ReflectionUtils;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public abstract class ToggleableFeature extends AbstractFeature {
    private volatile boolean enabled; // volatile for thread safety
    private Feature info;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        info = ReflectionUtils.getFeatureInfo(getClass());

        // get arguments and create toggle command
        var arguments = ReflectionUtils.getArguments(getClass());

        var baseCommand = literal(info.command())
                .executes(this::toggle);

        // add option configurations
        for (Argument argument : arguments) {
            String name = argument.getName();
            baseCommand.then(literal(name)
                    .executes(context -> {
                        Object value = argument.getDefaultValue();

                        // toggle if its a boolean
                        if (argument.getType().getClass() == BoolSerializer.class &&
                                argument.getValue() != null) {
                            value = !((Boolean)argument.getValue());
                        }

                        argument.setValue(value);
                        return Feedback.success("Set {} to {} for feature {}", name, value, info.name()).send(context);
                    })
                    .then(argument("value", argument.getType()).executes(context -> {
                        Object value = context.getArgument("value", Object.class);
                        argument.setValue(value);
                        return Feedback.success("Set {} to {} for feature {}", name, value, info.name()).send(context);
                    }))
            );
        }

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
