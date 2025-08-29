package tools.redstone.redstonetools.malilib;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.hotkeys.*;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;

public class KeybindHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
	private final MacroBase macro;

	public KeybindHandler(MacroBase macro) {
		super();
		this.macro = macro;
	}

	@Override
	public void addKeysToMap(IKeybindManager manager) {
		manager.addKeybindToMap(macro.hotkey.getKeybind());
	}

	@Override
	public void addHotkeys(IKeybindManager manager) {
		ImmutableList<IHotkey> IL = ImmutableList.of(macro.hotkey);
		manager.addHotkeysForCategory(RedstoneTools.MOD_NAME, macro.getName(), IL);
	}
}