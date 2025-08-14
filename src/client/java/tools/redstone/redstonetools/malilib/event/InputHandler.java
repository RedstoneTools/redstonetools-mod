package tools.redstone.redstonetools.malilib.event;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.hotkeys.*;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.malilib.config.Configs;

import java.util.List;

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
		for (IConfigBase config : Configs.General.OPTIONS) {
			if (config instanceof IHotkey hotkey)
				manager.addKeybindToMap(hotkey.getKeybind());
		}
	}

	@Override
	public void addHotkeys(IKeybindManager manager) {
		List<IHotkey> hotkeys = Configs.General.OPTIONS.stream()
				.filter(IHotkey.class::isInstance)
				.map(IHotkey.class::cast)
				.toList();
		manager.addHotkeysForCategory(RedstoneTools.MOD_NAME, "Generic", hotkeys);
	}
}