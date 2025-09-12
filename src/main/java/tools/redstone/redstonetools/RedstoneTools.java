package tools.redstone.redstonetools;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.redstone.redstonetools.packets.RedstoneToolsPackets;

public class RedstoneTools implements ModInitializer {
	public static final String MOD_ID = "redstonetools";
	public static final String MOD_NAME = "Redstone tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(RedstoneTools.MOD_ID);

	@Override
	public void onInitialize() {
		RedstoneToolsPackets.registerPackets();
		RedstoneToolsGameRules.register();
		Commands.registerCommands();
	}
}
