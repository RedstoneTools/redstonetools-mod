package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;
import com.domain.redstonetools.utils.CommandUtils;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public abstract class CommandFeature<O extends Options> extends AbstractFeature<O> {
    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        var info = ReflectionUtils.getFeatureInfo(this);

        CommandUtils.register(
                info.name(),
                getArguments(),
                context -> {
                    try {
                        var argumentObj = ReflectionUtils.getArgumentInstance(this);

                        for (var argument : ReflectionUtils.getArguments(argumentObj)) {
                            argument.setValue(context);
                        }

                        return execute(context.getSource(), argumentObj);
                    } catch (Throwable t) {
                        context.getSource().sendFeedback(Text.of("").getWithStyle(Style.EMPTY.withColor(Formatting.RED))
                                .get(0), false);
                        System.err.println("An uncaught error occurred in command '" + info.name() + "'");
                        t.printStackTrace();
                        return -1;
                    }
                },
                dispatcher,
                dedicated);
    }

    private List<Argument<?>> getArguments() {
        return List.of(ReflectionUtils.getArguments(ReflectionUtils.getArgumentInstance(this)));
    }

    protected abstract int execute(ServerCommandSource source, O options) throws CommandSyntaxException;
}
