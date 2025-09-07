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
import net.minecraft.text.Text;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.action.CommandListWidget;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;
import tools.redstone.redstonetools.utils.GuiUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GuiMacroEditor extends Screen {
	private final MacroBase macro;
	private final GuiMacroManager parent;
	private CommandListWidget commandList;
	private ConfigButtonKeybind buttonKeybind;
	private ConfigButtonBoolean buttonEnabled;
	private WidgetKeybindSettings widgetAdvancedKeybindSettings;
	private IConfigBoolean configBoolean;
	private TextFieldWidget nameWidget;
	private float errorCountDown;

	public GuiMacroEditor(Text title, MacroBase macro, GuiMacroManager parent) {
		super(title);
		this.parent = parent;
		this.client = MinecraftClient.getInstance();
		this.macro = macro;
	}

	private static Method bkRenderMethod;
	private static Method beRenderMethod;
	private static Method waksRenderMethod;


	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		buttonKeybind.updateDisplayString();
		try {
			buttonKeybind.render(context, mouseX, mouseY, buttonKeybind.isMouseOver(mouseX, mouseY));
			buttonEnabled.render(context, mouseX, mouseY, buttonEnabled.isMouseOver(mouseX, mouseY));
			widgetAdvancedKeybindSettings.render(context, mouseX, mouseY, buttonEnabled.isMouseOver(mouseX, mouseY));
		} catch (NoSuchMethodError ignored) {
			if (bkRenderMethod == null) {
				try {
					bkRenderMethod = ConfigButtonKeybind.class.getMethod("render", int.class, int.class, boolean.class, DrawContext.class);
					beRenderMethod = ConfigButtonBoolean.class.getMethod("render", int.class, int.class, boolean.class, DrawContext.class);
					waksRenderMethod = WidgetKeybindSettings.class.getMethod("render", int.class, int.class, boolean.class, DrawContext.class);
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong. Contact a redstonetools developer", e);
				}
			}
			try {
				bkRenderMethod.invoke(buttonKeybind, mouseX, mouseY, buttonKeybind.isMouseOver(mouseX, mouseY), context);
				beRenderMethod.invoke(buttonEnabled, mouseX, mouseY, buttonEnabled.isMouseOver(mouseX, mouseY), context);
				waksRenderMethod.invoke(widgetAdvancedKeybindSettings, mouseX, mouseY, widgetAdvancedKeybindSettings.isMouseOver(mouseX, mouseY), context);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Something went wrong. Contact a redstonetools developer", e);
			}
		}
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
		List<GuiUtils.Layout> layouts = GuiUtils.betterGetWidgetLayout(minMaxLayouts, 10, this.width, true, 50, this.height - 52, 20);
		GuiUtils.Layout ncLayout = layouts.get(0);
		GuiUtils.Layout bkLayout = layouts.get(1);
		GuiUtils.Layout akoLayout = layouts.get(2);
		GuiUtils.Layout beLayout = layouts.get(3);
		GuiUtils.Layout nwLayout = layouts.get(4);
		this.commandList = this.addDrawableChild(
			new CommandListWidget(this, this.client, this.width, this.height - 75, 0, 36, this.macro));
		this.addDrawableChild(ButtonWidget.builder(Text.of("Add command"), button ->
				this.commandList.addEntry())
			.dimensions(ncLayout.x(), ncLayout.y(), ncLayout.width(), ncLayout.height())
			.build());
		this.widgetAdvancedKeybindSettings = new WidgetKeybindSettings(akoLayout.x(), akoLayout.y(), akoLayout.width(), akoLayout.height(), macro.hotkey.getKeybind(), "", null, null) {
			@Override
			protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
				if (mouseButton == 0) {
					GuiBase.openGui(new GuiKeybindSettings(this.keybind, this.keybindName, null, fi.dy.masa.malilib.util.GuiUtils.getCurrentScreen()) {
						@Override
						public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
							if (this.client != null && this.client.world == null) this.renderPanoramaBackground(drawContext, partialTicks);
							this.applyBlur(drawContext);
							super.render(drawContext, mouseX, mouseY, partialTicks);
						}
					});
					return true;
				} else return super.onMouseClickedImpl(mouseX, mouseY, mouseButton);
			}
		};
		this.buttonKeybind = new ConfigButtonKeybind(bkLayout.x(), bkLayout.y(), bkLayout.width(), bkLayout.height(), macro.hotkey.getKeybind(), null) {
			@Override
			public boolean onMouseClicked(int mx, int my, int mb) {
				if (!this.isMouseOver(mx, my)) {
					this.selected = false;
					return false;
				} else {
					return super.onMouseClicked(mx, my, mb);
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
		this.buttonEnabled = new ConfigButtonBoolean(beLayout.x(), beLayout.y(), beLayout.width(), beLayout.height(), this.configBoolean);
		this.nameWidget = addDrawableChild(new TextFieldWidget(this.textRenderer, nwLayout.width(), nwLayout.height(), Text.of("")));
		this.nameWidget.setText(macro.getName());
		this.nameWidget.setPosition(nwLayout.x(), nwLayout.y());
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		buttonEnabled.onKeyTyped(keyCode, scanCode, modifiers);
		widgetAdvancedKeybindSettings.onKeyTyped(keyCode, scanCode, modifiers);
		buttonKeybind.onKeyPressed(keyCode);
		if (buttonKeybind.isSelected() && keyCode == 256) {
			this.macro.hotkey.getKeybind().clearKeys();
			buttonKeybind.onClearSelection();
			return true;
		}
		if (this.commandList.keyPressed(keyCode, scanCode, modifiers))
			return true;
		else
			return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		commandList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (buttonKeybind.onMouseClicked((int) mouseX, (int) mouseY, button)) {
			if (this.getFocused() != null) {
				this.getFocused().setFocused(false);
			}
			return true;
		}
		else if (buttonEnabled.onMouseClicked((int) mouseX, (int) mouseY, button)) {
			if (this.getFocused() != null) {
				this.getFocused().setFocused(false);
			}
			return true;
		}
		else if (widgetAdvancedKeybindSettings.onMouseClicked((int) mouseX, (int) mouseY, button)) {
			if (this.getFocused() != null) {
				this.getFocused().setFocused(false);
			}
			return true;
		}
		else if (super.mouseClicked(mouseX, mouseY, button)) return true;
		else return commandList.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		buttonKeybind.onMouseReleased((int) mouseX, (int) mouseY, button);
		buttonEnabled.onMouseReleased((int) mouseX, (int) mouseY, button);
		widgetAdvancedKeybindSettings.onMouseReleased((int) mouseX, (int) mouseY, button);
		if (commandList.mouseReleased(mouseX, mouseY, button)) return true;
		else return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (commandList.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) return true;
		else return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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
		else return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (commandList.keyReleased(keyCode, scanCode, modifiers)) return true;
		else return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (commandList.charTyped(chr, modifiers)) return true;
		else if (buttonKeybind.onCharTyped(chr, modifiers)) return true;
		else if (buttonEnabled.onCharTyped(chr, modifiers)) return true;
		else if (widgetAdvancedKeybindSettings.onCharTyped(chr, modifiers)) return true;
		else return super.charTyped(chr, modifiers);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (commandList.isMouseOver(mouseX, mouseY)) return true;
		else if (buttonKeybind.isMouseOver((int) mouseX, (int) mouseY)) return true;
		else if (buttonEnabled.isMouseOver((int) mouseX, (int) mouseY)) return true;
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
		this.macro.setName(this.nameWidget.getText());
		MacroManager.saveChanges();
		assert client != null;
		parent.initGui();
		GuiBase.openGui(parent);
	}
}
