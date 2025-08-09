package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.macros.actions.Action;
import tools.redstone.redstonetools.macros.actions.CommandAction;

import java.util.AbstractList;
import java.util.List;

public class MacroBase {
	protected final MinecraftClient mc;

	protected String name;
	protected boolean enabled;
	public IKeybind keybind;
	public List<CommandAction> actions;
	public List<String> actionsAsStringList = new AbstractList<>() {
		@Override
		public String get(int index) {
			return actions.get(index).command;
		}
		@Override
		public String set(int index, String element) {
			CommandAction action = actions.get(index);
			String old = action.command;
			action.command = element;
			return old;
		}
		@Override
		public int size() {
			return actions.size();
		}
		@Override
		public void add(int index, String element) {
			actions.add(index, new CommandAction(element));
		}
		@Override
		public String remove(int index) {
			String old = actions.get(index).command;
			actions.remove(index);
			return old;
		}
	};
	transient protected boolean needsUpdate;

	public MacroBase(String name, String keybind, List<CommandAction> actions) {
		this.actions = actions;
		this.keybind = KeybindMulti.fromStorageString(keybind, KeybindSettings.DEFAULT);
		this.mc = MinecraftClient.getInstance();
		this.name = name;
		this.needsUpdate = true;
		this.enabled = true;
	}

	public MacroBase(String name, String keybind, List<CommandAction> actions, boolean enabled) {
		this.actions = actions;
		this.keybind = KeybindMulti.fromStorageString(keybind, KeybindSettings.DEFAULT);
		this.mc = MinecraftClient.getInstance();
		this.name = name;
		this.needsUpdate = true;
		this.enabled = enabled;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean setEnabled(boolean enabled) {
		boolean oldEnabled = this.enabled;
		this.enabled = enabled;
		return  oldEnabled;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void toggleEnabled() {
		this.enabled = !this.enabled;

		if (this.enabled) {
			this.setNeedsUpdate();
		}
	}

	public void setNeedsUpdate() {
		this.needsUpdate = true;
	}

	public void run() {
		if (!enabled) {
			return;
		}

		for (Action action : actions) {
			action.run();
		}
	}
}
