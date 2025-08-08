package tools.redstone.redstonetools.malilib.event;

import fi.dy.masa.malilib.hotkeys.*;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.malilib.config.Configs;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
	private static final InputHandler INSTANCE = new InputHandler();

	private InputHandler() {
		super();
	}

	public static InputHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void addKeysToMap(IKeybindManager manager) {
		for (IHotkey hotkey : Configs.General.OPTIONS) {
			manager.addKeybindToMap(hotkey.getKeybind());
		}
	}

	@Override
	public void addHotkeys(IKeybindManager manager) {
		manager.addHotkeysForCategory(RedstoneTools.MOD_NAME, "Generic", Configs.General.OPTIONS);
	}
}