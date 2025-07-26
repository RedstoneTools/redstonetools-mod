package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.macros.MacroManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class MacroFeature extends AbstractFeature {
    public static void registerCommand() {
        EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("macro")
                .then(ClientCommandManager.argument("macro", StringArgumentType.string())
                .executes(context -> FeatureUtils.getFeature(MacroFeature.class).execute(context)))));
    }
    protected int execute(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String macro = StringArgumentType.getString(context, "macro");
        var macroObj = MacroManager.getMacro(macro);

        if (macroObj == null) {
            throw new SimpleCommandExceptionType(Text.literal("Macro \"%s\" does not exist.".formatted(macro))).create();
        }

        macroObj.run();
        return 1;
    }
}
