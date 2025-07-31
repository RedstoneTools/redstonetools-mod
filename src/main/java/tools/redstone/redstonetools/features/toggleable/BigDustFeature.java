package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BigDustFeature extends ToggleableFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("bigdust")
                .executes(context -> FeatureUtils.getFeature(BigDustFeature.class).toggle(context))
                .then(argument("heightInPixels", IntegerArgumentType.integer(1, 16))
                .executes(context -> FeatureUtils.getFeature(BigDustFeature.class).toggle(context)))));
    }
    public static int heightInPixels;
    @Override

    public int toggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean hasArguments = true;
        try {
            heightInPixels = IntegerArgumentType.getInteger(context, "heightInPixels");
        } catch (Exception e) {
            heightInPixels = 3;
            hasArguments = false;
        }
        if (hasArguments && isEnabled()) {
            return 1;
        }
        super.toggle(context);
        return 1;
    }
}
