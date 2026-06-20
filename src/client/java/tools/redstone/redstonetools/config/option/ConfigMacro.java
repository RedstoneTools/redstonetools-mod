package tools.redstone.redstonetools.config.option;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import kr1v.malilibApi.MalilibApi;
import kr1v.malilibApi.config._new.CustomConfigBase;
import kr1v.malilibApi.interfaces.IHotkeyContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import tools.redstone.redstonetools.config.MacroManager;
import tools.redstone.redstonetools.macros.actions.Action;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigMacro extends CustomConfigBase<ConfigMacro, Void> implements IHotkeyContainer {
	private String macroName;
	private boolean enabled;
	private boolean muted;
	private List<CommandAction> actions;
	private final IHotkey hotkey;

	public ConfigMacro(String macroName, boolean enabled, boolean muted, List<CommandAction> actions, String keybind, KeybindSettings settings) {
		this("",  macroName, enabled, muted, actions, keybind, settings, "", "", "");
	}

	public ConfigMacro(String name, String macroName, boolean enabled, boolean muted, List<CommandAction> actions, String keybind, KeybindSettings settings, String comment, String translatedName, String prettyName) {
		super(name, comment, translatedName, prettyName);
		this.macroName = macroName;
		this.enabled = enabled;
		this.muted = muted;
		this.actions = new ArrayList<>(actions);
		this.hotkey = new ConfigHotkey("", keybind, settings, "", "", "");
		this.hotkey.getKeybind().setCallback((t, g) -> {
			this.run();
			return true;
		});
	}

	@Override
	public void setValueFromJsonElement(JsonElement element) {
		try {
			JsonObject object = element.getAsJsonObject();

			boolean muted = object.get("muted").getAsBoolean();
			String name = object.get("name").getAsString();
			boolean enabled = object.get("enabled").getAsBoolean();
			List<CommandAction> actions = object
				.get("actions")
				.getAsJsonArray()
				.asList()
				.stream()
				.map(e -> new CommandAction(e.getAsString()))
				.toList();
			KeybindSettings settings = KeybindSettings.fromJson(object.get("settings").getAsJsonObject());
			String keys = object.get("keys").getAsString();

			this.setMuted(muted);
			this.setMacroName(name);
			this.setEnabled(enabled);
			this.setActions(actions);
			this.setSettings(settings);
			this.setKeybind(keys);
		} catch (Exception e) {
			System.out.println("Couldn't set json value for " + this.getName() + e);
		}
	}

	@Override
	public JsonElement getAsJsonElement() {
		JsonObject object = new JsonObject();

		JsonArray actionsArray = new JsonArray();
		actionsArray.asList().addAll(getActions().stream().map(action -> action.command).map(JsonPrimitive::new).toList());

		object.addProperty("muted", isMuted());
		object.addProperty("name", getMacroName());
		object.addProperty("enabled", isEnabled());
		object.addProperty("keys", getKeybind());
		object.add("settings", getSettings().toJson());
		object.add("actions", actionsArray);

		return object;
	}

	@Override
	public boolean isModified() {
		if (this.isMuted()) return true;
		if (!this.getActions().isEmpty()) return true;
		if (!this.isEnabled()) return true;
		if (!this.getKeybind().isEmpty()) return true;
		return false;
	}

	@Override
	public void resetToDefault() {
		this.hotkey.resetToDefault();
		this.hotkey.getKeybind().resetSettingsToDefaults();
		this.actions = new ArrayList<>();
		this.muted = false;
		this.enabled = true;
	}

	static {
		MalilibApi.registerButtonBasedConfigType(ConfigMacro.class, (widgetConfigOption, configMacro, x, y, configWidth, configHeight) -> new ButtonGeneric(x, y, configWidth, configHeight, configMacro.getMacroName()) {
			@Override
			protected boolean onMouseClickedImpl(/*? if >=1.21.10 {*/net.minecraft.client.input.MouseButtonEvent click, boolean doubleClick/*? } else {*//*int mouseX, int mouseY, int mouseButton*//*? }*/) {
				super.onMouseClickedImpl(/*? if >=1.21.10 {*/click, doubleClick/*? } else {*//*mouseX, mouseY, mouseButton*//*? }*/);
				GuiBase.openGui(new GuiMacroEditor(Component.nullToEmpty(configMacro.macroName), configMacro, Minecraft.getInstance().screen));
				return true;
			}
		});
	}

	@Override
	public List<IHotkey> getHotkeys() {
		return List.of(this.hotkey);
	}

	@Override
	public String toString() {
		return this.getMacroName();
	}

	public boolean isMuted() {
		return this.muted;
	}
	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMacroName() {
		return this.macroName;
	}
	public void setMacroName(String name) {
		this.macroName = name;
	}

	public List<CommandAction> getActions() {
		return this.actions;
	}
	public void setActions(List<CommandAction> actions) {
		this.actions.clear();
		this.actions.addAll(actions);
	}

	public String getKeybind() {
		return this.hotkey.getStringValue();
	}
	public void setKeybind(String keybind) {
		this.hotkey.getKeybind().setValueFromString(keybind);
	}

	public KeybindSettings getSettings() {
		return this.hotkey.getKeybind().getSettings();
	}
	public void setSettings(KeybindSettings settings) {
		this.hotkey.getKeybind().setSettings(settings);
	}

	private final AtomicInteger layers = new AtomicInteger(0);

	public void run() {
		if (muted) MacroManager.shouldMute = true;
		if (!enabled) return;
		if (layers.getAndSet(layers.get() + 1) > 100) {
			assert Minecraft.getInstance().player != null;
			//? if <26.1 {
			/*Minecraft.getInstance().player.displayClientMessage(Component.literal("Please don't cause a stackoverflow :("), false);
			 *///? } else
			Minecraft.getInstance().player.sendSystemMessage(Component.literal("Please don't cause a stackoverflow :("));
			return;
		}
		try {
			for (Action action : actions) {
				action.run();
			}
		} catch (StackOverflowError ignored) {
			try {
				assert Minecraft.getInstance().player != null;
				//? if <26.1 {
				/*Minecraft.getInstance().player.displayClientMessage(Component.literal("Please don't cause a stackoverflow :("), false);
				 *///? } else
				Minecraft.getInstance().player.sendSystemMessage(Component.literal("Please don't cause a stackoverflow :("));
			} catch (NoClassDefFoundError e) {
				// yeah we are absolutely cooked, there is no way to recover from this. I'm not even sure this can happen
				// actually there's probably a better throwable to be thrown here. whatever.
				throw new InternalError("Something has gone terribly wrong. Shouldn't have run a macro that runs itself.", e);
			}
		} finally {
			layers.set(0);
		}
		if (muted) MacroManager.shouldMute = false;
	}


	@Override
	public Void get() {
		return null;
	}

	@Override
	public Void getDefault() {
		return null;
	}

	@Override
	public void set(Void value) {
	}
}
