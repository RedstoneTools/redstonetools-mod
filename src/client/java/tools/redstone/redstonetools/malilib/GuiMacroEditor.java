package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiKeybindSettings;
import fi.dy.masa.malilib.gui.button.ConfigButtonBoolean;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.widgets.WidgetKeybindSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
/*$ click_and_inputs_imports {*/



/*$}*/
import net.minecraft.text.Text;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.action.CommandListWidget;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;
import tools.redstone.redstonetools.utils.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiMacroEditor extends Screen {
	public final MacroBase macro;
	private final GuiMacroManager parent;
	private CommandListWidget commandList;
	private ConfigButtonKeybind buttonKeybind;
	private ConfigButtonBoolean buttonEnabled;
	private ConfigButtonBoolean buttonMuted;
	private WidgetKeybindSettings widgetAdvancedKeybindSettings;
	private IConfigBoolean configBoolean;
	private IConfigBoolean configBoolean2;
	public TextFieldWidget nameWidget;
	private float errorCountDown;

	public GuiMacroEditor(Text title, MacroBase macro, GuiMacroManager parent) {
		super(title);
		this.parent = parent;
		this.client = MinecraftClient.getInstance();
		this.macro = macro;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		buttonKeybind.updateDisplayString();
		buttonEnabled.updateDisplayString();
		buttonMuted.updateDisplayString();
		//? if <=1.21.5 {
		buttonKeybind.render(mouseX, mouseY, buttonKeybind.isMouseOver(mouseX, mouseY), context);
		buttonEnabled.render(mouseX, mouseY, buttonEnabled.isMouseOver(mouseX, mouseY), context);
		buttonMuted.render(mouseX, mouseY, buttonMuted.isMouseOver(mouseX, mouseY), context);
		widgetAdvancedKeybindSettings.render(mouseX, mouseY, widgetAdvancedKeybindSettings.isMouseOver(mouseX, mouseY), context);
		//?} else {
		/*buttonKeybind.render(context, mouseX, mouseY, buttonKeybind.isMouseOver(mouseX, mouseY));
		buttonEnabled.render(context, mouseX, mouseY, buttonEnabled.isMouseOver(mouseX, mouseY));
		buttonMuted.render(context, mouseX, mouseY, buttonMuted.isMouseOver(mouseX, mouseY));
		widgetAdvancedKeybindSettings.render(context, mouseX, mouseY, widgetAdvancedKeybindSettings.isMouseOver(mouseX, mouseY));
		*///?}
		if (errorCountDown > 0.0f) {
			context.drawText(this.textRenderer, "Name already exists!", mouseX, mouseY - 10, 0xFFFFFFFF, true);
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
		GuiUtils.Layout ncLayout = layouts.get(0);
		GuiUtils.Layout bkLayout = layouts.get(1);
		GuiUtils.Layout akoLayout = layouts.get(2);
		GuiUtils.Layout beLayout = layouts.get(3);
		GuiUtils.Layout nwLayout = layouts.get(4);
		GuiUtils.Layout bmLayout = layouts.get(5);
		this.commandList = this.addDrawableChild(
			new CommandListWidget(this, this.client, this.width, this.height - 75, 0, 36, this.macro));
		this.addDrawableChild(ButtonWidget.builder(Text.of("Add command"), button ->
				this.commandList.addEntry())
			.dimensions(ncLayout.x(), ncLayout.y(), ncLayout.width(), ncLayout.height())
			.build());
		this.widgetAdvancedKeybindSettings = new WidgetKeybindSettings(akoLayout.x(), akoLayout.y(), akoLayout.width(), akoLayout.height(), macro.hotkey.getKeybind(), "", null, null) {
			@Override
			protected boolean onMouseClickedImpl(/*$ click_params {*/ int mouseX, int mouseY, int button/*$}*/) {
				//? if >=1.21.10 {
				/*int button = click.button();
				*///?}
				if (button == 0) {
					GuiBase.openGui(new GuiKeybindSettings(this.keybind, this.keybindName, null, fi.dy.masa.malilib.util.GuiUtils.getCurrentScreen()));
					return true;
				} else return super.onMouseClickedImpl(/*$ click_args {*/mouseX, mouseY, button/*$}*/);
			}
		};
		this.buttonKeybind = new ConfigButtonKeybind(bkLayout.x(), bkLayout.y(), bkLayout.width(), bkLayout.height(), macro.hotkey.getKeybind(), null) {
			@Override
			public boolean onMouseClicked(/*$ click_params {*/int mouseX, int mouseY, int button/*$}*/) {
				if (!this.isMouseOver(/*? if >=1.21.10 {*/ /*(int) click.x(), (int) click.y() *//*?} else {*/ mouseX, mouseY /*?}*/)) {
					this.selected = false;
					return false;
				} else {
					return super.onMouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/);
				}
			}
			@Override
			public void onClearSelection() {
				this.firstKey = true;
				super.onClearSelection();
			}
		};
		this.configBoolean = new ConfigBoolean("", true, "");
		this.configBoolean.setBooleanValue(this.macro.isEnabled());
		this.configBoolean2 = new ConfigBoolean("", true, "");
		this.configBoolean2.setBooleanValue(this.macro.muted);
		final String beName = "Enabled: ";
		final String bmName = "Muted: ";
		this.buttonEnabled = new ConfigButtonBoolean(beLayout.x(), beLayout.y(), beLayout.width(), beLayout.height(), this.configBoolean) {
			@Override
			public void updateDisplayString() {
				super.updateDisplayString();
				this.displayString = beName + this.displayString;
			}
		};
		this.buttonMuted = new ConfigButtonBoolean(bmLayout.x(), bmLayout.y(), bmLayout.width(), bmLayout.height(), this.configBoolean2) {
			@Override
			public void updateDisplayString() {
				super.updateDisplayString();
				this.displayString = bmName + this.displayString;
			}
		};
		this.nameWidget = addDrawableChild(new TextFieldWidget(this.textRenderer, nwLayout.width(), nwLayout.height(), Text.of("")));
		this.nameWidget.setText(macro.getName());
		this.nameWidget.setPosition(nwLayout.x(), nwLayout.y());
	}

	@Override
	public boolean keyPressed(/*$ keyinput_params_vars2 {*/int keyCode, int scanCode, int modifiers) {/*$}*/
		buttonEnabled.onKeyTyped(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
		buttonMuted.onKeyTyped(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
		widgetAdvancedKeybindSettings.onKeyTyped(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
		buttonKeybind.onKeyPressed(keyCode);
		if (buttonKeybind.isSelected() && keyCode == 256) {
			this.macro.hotkey.getKeybind().clearKeys();
			buttonKeybind.onClearSelection();
			return true;
		}
		if (this.commandList.keyPressed(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/))
			return true;
		else
			return super.keyPressed(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		commandList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(/*$ click_params {*/int mouseX, int mouseY, int button/*$}*/) {
		if (buttonKeybind.onMouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/)) {
			if (this.getFocused() != null) {
				this.getFocused().setFocused(false);
			}
			return true;
		}
		else if (buttonEnabled.onMouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/)) {
			if (this.getFocused() != null) {
				this.getFocused().setFocused(false);
			}
			return true;
		}
		else if (buttonMuted.onMouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/)) {
			if (this.getFocused() != null) {
				this.getFocused().setFocused(false);
			}
			return true;
		}
		else if (widgetAdvancedKeybindSettings.onMouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/)) {
			if (this.getFocused() != null) {
				this.getFocused().setFocused(false);
			}
			return true;
		}
		else if (super.mouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/)) return true;
		else return commandList.mouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/);
	}

	@Override
	public boolean mouseReleased(/*$ click_nodouble_params {*/double mouseX, double mouseY, int button/*$}*/) {
		buttonKeybind.onMouseReleased(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/);
		buttonEnabled.onMouseReleased(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/);
		buttonMuted.onMouseReleased(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/);
		widgetAdvancedKeybindSettings.onMouseReleased(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/);
		if (commandList.mouseReleased(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/)) return true;
		else return super.mouseReleased(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/);
	}

	@Override
	public boolean mouseDragged(/*$ click_nodouble_params {*/double mouseX, double mouseY, int button/*$}*/, double deltaX, double deltaY) {
		if (commandList.mouseDragged(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/, deltaX, deltaY)) return true;
		else return super.mouseDragged(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (commandList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) return true;
		else if (buttonKeybind.onMouseScrolled((int) mouseX, (int) mouseY, horizontalAmount, verticalAmount))
			return true;
		else if (widgetAdvancedKeybindSettings.onMouseScrolled((int) mouseX, (int) mouseY, horizontalAmount, verticalAmount))
			return true;
		else if (buttonEnabled.onMouseScrolled((int) mouseX, (int) mouseY, horizontalAmount, verticalAmount))
			return true;
		else if (buttonMuted.onMouseScrolled((int) mouseX, (int) mouseY, horizontalAmount, verticalAmount))
			return true;
		else return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyReleased(/*$ keyinput_params {*/int keyCode, int scanCode, int modifiers/*$}*/) {
		if (commandList.keyReleased(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/)) return true;
		else return super.keyReleased(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
	}

	@Override
	public boolean charTyped(/*$ charinput_params {*/char chr, int modifiers/*$}*/) {
		if (commandList.charTyped(/*$ charinput_args {*/chr, modifiers/*$}*/)) return true;
		else if (buttonKeybind.onCharTyped(/*$ charinput_args {*/chr, modifiers/*$}*/)) return true;
		else if (buttonEnabled.onCharTyped(/*$ charinput_args {*/chr, modifiers/*$}*/)) return true;
		else if (buttonMuted.onCharTyped(/*$ charinput_args {*/chr, modifiers/*$}*/)) return true;
		else if (widgetAdvancedKeybindSettings.onCharTyped(/*$ charinput_args {*/chr, modifiers/*$}*/)) return true;
		else return super.charTyped(/*$ charinput_args {*/chr, modifiers/*$}*/);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (commandList.isMouseOver(mouseX, mouseY)) return true;
		else if (buttonKeybind.isMouseOver((int) mouseX, (int) mouseY)) return true;
		else if (buttonEnabled.isMouseOver((int) mouseX, (int) mouseY)) return true;
		else if (buttonMuted.isMouseOver((int) mouseX, (int) mouseY)) return true;
		else if (widgetAdvancedKeybindSettings.isMouseOver((int) mouseX, (int) mouseY)) return true;
		else return super.isMouseOver(mouseX, mouseY);
	}

	@Override
	public void close() {
		if (MacroManager.nameExists(this.nameWidget.getText(), this.macro)) {
			errorCountDown = 50.0f;
			return;
		}
		this.macro.actions.clear();
		this.commandList.children().forEach(t -> this.macro.actions.add(t.command));
		this.macro.setEnabled(this.configBoolean.getBooleanValue());
		this.macro.muted = this.configBoolean2.getBooleanValue();
		this.macro.setName(this.nameWidget.getText());
		MacroManager.saveChanges();
		assert client != null;
		parent.initGui();
		GuiBase.openGui(parent);
	}
}
