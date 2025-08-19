package tools.redstone.redstonetools;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import static tools.redstone.redstonetools.RedstoneTools.LOGGER;

public class RedstoneToolsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Redstone Tools");
		// check if malilib present, if not open an error window and throw
		if (!FabricLoader.getInstance().isModLoaded("malilib")) {
			TinyFileDialogs.tinyfd_messageBox(
					"Error",
					"MaLiLib not present!\nPlease install MaLiLib if you want to use redstonetools",
					"ok",
					"error",
					false
			);
			throw new IllegalStateException("MaLiLib not present");
		}
		Init.init();
	}
}
