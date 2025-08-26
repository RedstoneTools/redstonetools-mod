package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ConfigButtonBoolean;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.action.CommandListWidget;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;

public class GuiMacroEditor2 extends Screen {
	private final MacroBase macro;
	private final Screen parent;
	private CommandListWidget commandList;
	private ConfigButtonKeybind buttonKeybind;
	private ConfigButtonBoolean buttonEnabled;
	private IConfigBoolean configBoolean;
	private TextFieldWidget nameWidget;

	public GuiMacroEditor2(Text title, MacroBase macro, Screen parent) {
		super(title);
		this.parent = parent;
		this.client = MinecraftClient.getInstance();
		this.macro = macro;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		buttonKeybind.updateDisplayString();
		buttonKeybind.render(context, mouseX, mouseY, buttonKeybind.isMouseOver(mouseX, mouseY));
		buttonEnabled.render(context, mouseX, mouseY, buttonEnabled.isMouseOver(mouseX, mouseY));
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		buttonEnabled.onKeyTyped(keyCode, scanCode, modifiers);
		buttonKeybind.onKeyPressed(keyCode);
		if (this.commandList.keyPressed(keyCode, scanCode, modifiers))
			return true;
		else
			return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	protected void init() {
		this.commandList = this.addDrawableChild(
			new CommandListWidget(this, this.client, this.width, this.height - 75, 0, 36, this.macro)
		);
		this.addDrawableChild(ButtonWidget.builder(Text.of("New command"), button ->
				this.commandList.addEntry())
			.dimensions(this.width / 2 + 4, this.height - 52, 150, 20)
			.build());
		this.buttonKeybind = new ConfigButtonKeybind(10, this.height - 52, 150, 20, macro.hotkey.getKeybind(), null) {
			@Override
			public boolean onMouseClicked(int mx, int my, int mb) {
				if (!this.isMouseOver(mx, my)) {
					this.selected = false;
					return false;
				} else {
					return super.onMouseClicked(mx, my, mb);
				}
			}
		};
		this.configBoolean = new ConfigBoolean("", true, "");
		this.configBoolean.setBooleanValue(this.macro.isEnabled());
		this.buttonEnabled = new ConfigButtonBoolean(buttonKeybind.getX() + buttonKeybind.getWidth() + 10, this.height - 52, 150, 20, this.configBoolean);
		this.nameWidget = addDrawableChild(new TextFieldWidget(this.textRenderer, 150, 20, Text.of("")));
		this.nameWidget.setText(macro.getName());
		this.nameWidget.setPosition(buttonEnabled.getX() + buttonEnabled.getWidth() + 10, this.height - 52);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		commandList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (buttonKeybind.onMouseClicked((int) mouseX, (int) mouseY, button)) return true;
		else if (buttonEnabled.onMouseClicked((int) mouseX, (int) mouseY, button)) return true;
		else if (commandList.mouseClicked(mouseX, mouseY, button)) return true;
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		buttonKeybind.onMouseReleased((int)mouseX, (int)mouseY, button);
		buttonEnabled.onMouseReleased((int)mouseX, (int)mouseY, button);
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
		else if (buttonKeybind.onMouseScrolled((int)mouseX, (int) mouseY, horizontalAmount, verticalAmount)) return true;
		else if (buttonEnabled.onMouseScrolled((int)mouseX, (int) mouseY, horizontalAmount, verticalAmount)) return true;
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
		else return super.charTyped(chr, modifiers);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (commandList.isMouseOver(mouseX, mouseY)) return true;
		else if (buttonKeybind.isMouseOver((int)mouseX, (int)mouseY)) return true;
		else if (buttonEnabled.isMouseOver((int)mouseX, (int)mouseY)) return true;
		else return super.isMouseOver(mouseX, mouseY);
	}

	@Override
	public void close() {
		this.macro.actions.clear();
		this.commandList.children().forEach(t -> this.macro.actions.add(t.command));
		this.macro.setEnabled(this.configBoolean.getBooleanValue());
		this.macro.setName(this.nameWidget.getText());
		MacroManager.saveChanges();
		assert client != null;
		parent.init(client, parent.width, parent.height);
		GuiBase.openGui(parent);
	}
}
