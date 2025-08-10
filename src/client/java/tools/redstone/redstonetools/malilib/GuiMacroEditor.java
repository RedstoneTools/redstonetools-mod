package tools.redstone.redstonetools.malilib;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import net.minecraft.client.gui.DrawContext;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.MacroBase;
import tools.redstone.redstonetools.malilib.widget.WidgetListMacros;

import java.util.ArrayList;
import java.util.List;

public class GuiMacroEditor extends GuiConfigsBase {
	private final MacroBase macro;
	private final ConfigStringList commands;
	private final WidgetListMacros parent;
	private final GuiTextFieldGeneric errorText;

	public GuiMacroEditor(MacroBase macro, WidgetListMacros parent) {
		super(10, 50, RedstoneTools.MOD_ID, null, macro.getName(), "");
		this.parent = parent;
		this.macro = macro;
		this.title = macro.getName();
		this.commands = new ConfigStringList("Commands", ImmutableList.of());
		this.configEnabled = new ConfigBoolean("enabled", this.macro.isEnabled(), "Whether or not to enable the hotkey", "Enabled");
		this.configName = new ConfigString("name", this.macro.getName(), "Name of the macro", "Name");
		this.commands.setStrings(macro.actionsAsStringList);
		this.errorText = new GuiTextFieldGeneric(this.width / 2, 50, -1, 20, mc.textRenderer);
		this.errorText.visible = false;
	}

	@Override
	public void initGui() {
		super.initGui();

		int x = 10;

		ButtonGeneric button = new ButtonGeneric(x, this.height - 24, -1, 20, GuiConfigs.ConfigGuiTab.MACROS.getDisplayName());
		this.addButton(button, (a, b) -> updateConfigsAndClose());
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		super.render(drawContext, mouseX, mouseY, partialTicks);
	}

	@Override
	public void close() {
		updateConfigsAndClose();
	}

	private void updateConfigsAndClose() {
		if (MacroManager.nameExists(this.configName.getStringValue(), this.macro)) {
			this.errorText.setText("Name already exists!");
			this.errorText.visible = true;
			return;
		}
		this.macro.setName(this.configName.getStringValue());
		this.macro.setEnabled(this.configEnabled.getBooleanValue());
		this.macro.actions.clear();
		for (String s : this.commands.getStrings()) {
			this.macro.actions.add(new CommandAction(s));
		}
		MacroManager.saveChanges();
		this.parent.refreshEntries();
		GuiBase.openGui(new GuiMacroManager());
	}

	private final ConfigBoolean configEnabled;
	private final ConfigString configName;

	// I'm sorry
	@Override
	public List<ConfigOptionWrapper> getConfigs() {
		List<ConfigBoolean> configsB = new ArrayList<>();
		configsB.add(this.configEnabled);

		List<ConfigHotkey> configsH = new ArrayList<>();
		configsH.add(this.macro.hotkey);

		List<ConfigStringList> configsSL = new ArrayList<>();
		configsSL.add(this.commands);

		List<ConfigString> configsS = new ArrayList<>();
		configsS.add(this.configName);

		List<ConfigOptionWrapper> configOptionWrappers = new ArrayList<>();
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsS)));
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsH)));
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsSL)));
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsB)));
		return ImmutableList.copyOf(configOptionWrappers);
	}

	private <E> List<E> immutableListToList(List<E> aFor) {
		if (aFor instanceof ImmutableList<E> IL) {
			return new ArrayList<>(IL);
		} else {
			throw new IllegalStateException("Trying to convert a " + aFor.getClass() + " to List");
		}
	}
}