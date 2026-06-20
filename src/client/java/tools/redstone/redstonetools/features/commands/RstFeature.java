package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import kr1v.malilibApi.InternalMalilibApi;
import kr1v.malilibApi.MalilibApi;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.config.Macros;

//? if >=26.1 {
/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
*///? } else {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//? }

public class RstFeature {
	public static final RstFeature INSTANCE = new RstFeature();

	protected RstFeature() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(literal("rst")
				.executes(commandContext -> {
					Minecraft.getInstance().execute(() -> {
						if (InternalMalilibApi.getMod(RedstoneTools.MOD_ID).activeTab() == Macros.getTab()) {
							InternalMalilibApi.getMod(RedstoneTools.MOD_ID).setActiveTab(null);
						}
						MalilibApi.openScreenFor(RedstoneTools.MOD_ID);
					});
					return 1;
				}));
	}
}
