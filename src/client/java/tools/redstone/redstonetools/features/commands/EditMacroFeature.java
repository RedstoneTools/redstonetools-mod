package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import kr1v.malilibApi.InternalMalilibApi;
import kr1v.malilibApi.MalilibApi;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.config.Macros;


public class EditMacroFeature {
	public static final EditMacroFeature INSTANCE = new EditMacroFeature();

	protected EditMacroFeature() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(ClientCommandManager.literal("edit-macros")
				.executes(commandContext -> {
					Minecraft.getInstance().execute(() -> {
						InternalMalilibApi.getMod(RedstoneTools.MOD_ID).setActiveTab(Macros.getTab());
						MalilibApi.openScreenFor(RedstoneTools.MOD_ID);
					});

					return 1;
				}));
	}
}
