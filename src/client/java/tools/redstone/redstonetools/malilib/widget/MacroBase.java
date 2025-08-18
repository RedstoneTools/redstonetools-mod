package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.macros.actions.Action;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.KeybindHandler;

import java.util.AbstractList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MacroBase {
	protected final MinecraftClient mc;
	public ConfigHotkey hotkey;

	protected String name;
	protected boolean enabled;
	public KeybindHandler handler;
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
		this(name, keybind, actions, true);
	}

	public MacroBase(String name, String keybind, List<CommandAction> actions, boolean enabled) {
		this.actions = new java.util.ArrayList<>(actions);
		this.hotkey = new ConfigHotkey("hotkey", keybind, KeybindSettings.PRESS_ALLOWEXTRA, "Pressing this hotkey will activate the macro", "Hotkey");
		this.hotkey.getKeybind().setCallback((t, g) -> {
			this.run();
			return true;
		});
		this.mc = MinecraftClient.getInstance();
		this.name = name;
		this.needsUpdate = true;
		this.enabled = enabled;
		this.handler = new KeybindHandler(this);
		InputEventHandler.getKeybindManager().registerKeybindProvider(this.handler);
		InputEventHandler.getInputManager().registerKeyboardInputHandler(this.handler);
		InputEventHandler.getInputManager().registerMouseInputHandler(this.handler);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	private final AtomicInteger layers = new AtomicInteger(0);

	public void run() {
		if (!enabled) return;
		if (layers.getAndSet(layers.get() + 1) > 100) {
			MinecraftClient.getInstance().player.sendMessage(Text.of("Please don't cause a stackoverflow :("), false);
			return;
		}
		try {
			for (Action action : actions) {
				action.run();
			}
		} catch (StackOverflowError ignored) {
			try {
				MinecraftClient.getInstance().player.sendMessage(Text.of("Please don't cause a stackoverflow :("), false);
			} catch (NoClassDefFoundError e) {
				// yeah we are absolutely cooked, there is no way to recover from this. I'm not even sure this can happen
				// actually there's probably a better throwable to be thrown here. whatever.
				throw new InternalError("Something has gone terribly wrong. Shouldn't have run a macro that runs itself.", e);
			}
		} finally {
			layers.set(0);
		}
	}
}
