package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.commands.argument.MacroArgumentType;
import tools.redstone.redstonetools.macros.Macro;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class MacroFeature extends AbstractFeature {
    public static void registerCommand() {
        EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("macro")
                .then(ClientCommandManager.argument("macro", MacroArgumentType.macro())
                .executes(context -> ClientFeatureUtils.getFeature(MacroFeature.class).execute(context)))));
    }
    protected int execute(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Macro macro = MacroArgumentType.getMacro(context, "macro");
        macro.run();
        return 1;
    }
}
