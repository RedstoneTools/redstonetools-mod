package tools.redstone.redstonetools.features.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.macros.MacroManager;
import tools.redstone.redstonetools.macros.gui.malilib.MacrosScreen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class EditMacroFeature {
    public static void registerCommand() {
        EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("edit-macros")
                .executes(commandContext -> {
                    MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new MacrosScreen(MinecraftClient.getInstance().currentScreen, MacroManager.getMacros())));
                    return 1;
                })));
    }
}
