package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.malilib.event.InputHandler;

public class InitHandler implements IInitializationHandler {
	@Override
	public void registerModHandlers() {
		ConfigManager.getInstance().registerConfigHandler(RedstoneTools.MOD_ID, new Configs());

		Registry.CONFIG_SCREEN.registerConfigScreenFactory(
				new ModInfo(RedstoneTools.MOD_ID, RedstoneTools.MOD_NAME, GuiConfigs::new)
		);
		InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());
		Configs.General.HOTKEY_OPEN_GUI.getKeybind().setCallback((t, c) -> {
			GuiBase.openGui(new GuiConfigs());
			return true;
		});
	}
}