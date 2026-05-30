package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ConfigButtonBoolean;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.widgets.WidgetKeybindSettings;
//? if >=1.21.11 {
/*import fi.dy.masa.malilib.render.GuiContext;
*///?}
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import kr1v.malilibApi.InternalMalilibApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.config.MacroManager;
import tools.redstone.redstonetools.config.option.ConfigMacro;
import tools.redstone.redstonetools.malilib.widget.WidgetBaseWrapper;
import tools.redstone.redstonetools.malilib.widget.action.CommandListWidget;
import tools.redstone.redstonetools.utils.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiMacroEditor extends Screen {
	public final ConfigMacro macro;
	private final Screen parent;
	public CommandListWidget commandList;
	private final IConfigBoolean enabledConfigBoolean;
	private final IConfigBoolean mutedConfigBoolean;
	public EditBox nameWidget;
	private float errorCountDown;
	private final IKeybind keybind;
	private ConfigButtonKeybind buttonKeybind;

	public GuiMacroEditor(Component title, ConfigMacro macro, Screen parent) {
		super(title);
		this.parent = parent;
		this.macro = macro;

		// todo: swap the booleans to be normal buttons
		this.enabledConfigBoolean = new ConfigBoolean("", true, "");
		this.enabledConfigBoolean.setBooleanValue(this.macro.isEnabled());
		this.mutedConfigBoolean = new ConfigBoolean("", true, "");
		this.mutedConfigBoolean.setBooleanValue(this.macro.isMuted());
		this.keybind = KeybindMulti.fromStorageString(this.macro.getKeybind(), this.macro.getSettings());
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		if (errorCountDown > 0.0f) {
			context.drawString(this.font, "Name already exists!", mouseX, mouseY - 10, 0xFFFFFFFF, true);
			errorCountDown -= deltaTicks;
		}
	}

	@Override
	protected void init() {
		List<GuiUtils.MinMaxLayout> minMaxLayouts = new ArrayList<>();
		minMaxLayouts.add(new GuiUtils.MinMaxLayout(-1, -1, -1, -1)); // new command
		minMaxLayouts.add(new GuiUtils.MinMaxLayout(-1, -1, -1, -1)); // button keybind
		minMaxLayouts.add(new GuiUtils.MinMaxLayout(-1, 20, -1, -1)); // advanced keybinds option
		minMaxLayouts.add(new GuiUtils.MinMaxLayout(-1, -1, -1, -1)); // button enabled
		minMaxLayouts.add(new GuiUtils.MinMaxLayout(-1, -1, -1, -1)); // name widget
		minMaxLayouts.add(new GuiUtils.MinMaxLayout(-1, -1, -1, -1)); // mute
		List<GuiUtils.Layout> layouts = GuiUtils.betterGetWidgetLayout(minMaxLayouts, 10, this.width, true, 50, this.height - 52, 20);
		GuiUtils.Layout addCommandLayout = layouts.get(0);
		GuiUtils.Layout buttonKeybindLayout = layouts.get(1);
		GuiUtils.Layout keybindSettingsLayout = layouts.get(2);
		GuiUtils.Layout buttonEnabledLayout = layouts.get(3);
		GuiUtils.Layout nameWidgetLayout = layouts.get(4);
		GuiUtils.Layout buttonMutedLayout = layouts.get(5);

		this.commandList = this.addRenderableWidget(new CommandListWidget(this, this.minecraft, this.width, this.height - 75, 0, 36, this.macro));
		this.addRenderableWidget(Button.builder(Component.nullToEmpty("Add command"), button -> this.commandList.addEntry())
			.bounds(addCommandLayout.x(), addCommandLayout.y(), addCommandLayout.width(), addCommandLayout.height())
			.build());

		WidgetKeybindSettings widgetAdvancedKeybindSettings = new WidgetKeybindSettings(keybindSettingsLayout.x(), keybindSettingsLayout.y(), keybindSettingsLayout.width(), keybindSettingsLayout.height(), keybind, "", null, null);
		this.buttonKeybind = new ConfigButtonKeybind(buttonKeybindLayout.x(), buttonKeybindLayout.y(), buttonKeybindLayout.width(), buttonKeybindLayout.height(), keybind, null);

		ConfigButtonBoolean buttonEnabled = new ConfigButtonBoolean(buttonEnabledLayout.x(), buttonEnabledLayout.y(), buttonEnabledLayout.width(), buttonEnabledLayout.height(), this.enabledConfigBoolean) {
			@Override
			public void updateDisplayString() {
				super.updateDisplayString();
				this.displayString = "Enabled: " + this.displayString;
			}
		};
		ConfigButtonBoolean buttonMuted = new ConfigButtonBoolean(buttonMutedLayout.x(), buttonMutedLayout.y(), buttonMutedLayout.width(), buttonMutedLayout.height(), this.mutedConfigBoolean) {
			@Override
			public void updateDisplayString() {
				super.updateDisplayString();
				this.displayString = "Muted: " + this.displayString;
			}
		};

		this.addRenderableWidget(new WidgetBaseWrapper(buttonMuted));
		this.addRenderableWidget(new WidgetBaseWrapper(widgetAdvancedKeybindSettings));
		this.addRenderableWidget(new WidgetBaseWrapper(buttonKeybind));
		this.addRenderableWidget(new WidgetBaseWrapper(buttonEnabled));
		this.nameWidget = addRenderableWidget(new EditBox(this.font, nameWidgetLayout.width(), nameWidgetLayout.height(), Component.nullToEmpty("")));
		this.nameWidget.setValue(macro.getMacroName());
		this.nameWidget.setPosition(nameWidgetLayout.x(), nameWidgetLayout.y());
	}

	//? if <=1.21.8 {
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.buttonKeybind.isMouseOver((int) mouseX, (int) mouseY)) {
			this.buttonKeybind.onClearSelection();
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	//? } else {
	/*@Override
	public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent click, boolean doubled) {
		if (!this.buttonKeybind.isMouseOver((int) click.x(), (int) click.y())) {
			this.buttonKeybind.onClearSelection();
		}
		return super.mouseClicked(click, doubled);
	}
	*///? }

	@Override
	public void onClose() {
		if (MacroManager.nameExists(this.nameWidget.getValue(), this.macro)) {
			errorCountDown = 50.0f;
			return;
		}

		macro.setActions(this.commandList.children().stream().map(m -> m.command).toList());
		macro.setEnabled(this.enabledConfigBoolean.getBooleanValue());
		macro.setMuted(this.mutedConfigBoolean.getBooleanValue());
		macro.setMacroName(this.nameWidget.getValue());
		macro.setKeybind(this.keybind.getStringValue());
		macro.setSettings(this.keybind.getSettings());

		InputEventHandler.getKeybindManager().updateUsedKeys();
		GuiBase.openGui(parent);

		// malilib forces a load of the configs from disk when joining a world, and
		// if settings are changed while on the title screen, the settings are never saved
		// hmmm... this also happens with normal configs, not just macros
		// I'll just hope nobody notices
		Minecraft.getInstance().schedule(() -> InternalMalilibApi.getMod(RedstoneTools.MOD_ID).configHandler.save());
	}
}
