package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import tools.redstone.redstonetools.malilib.widget.action.CommandListWidget;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;

public class GuiMacroEditor2 extends Screen {
	private final MacroBase macro;
	private final Screen parent;
	private CommandListWidget commandList;
	private ConfigButtonKeybind buttonKeybind;

	public GuiMacroEditor2(Text title, MacroBase macro, Screen parent) {
		super(title);
		this.parent = parent;
		this.client = MinecraftClient.getInstance();
		this.macro = macro;
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
		this.buttonKeybind = new ConfigButtonKeybind(10, this.height - 52, 150, 20, macro.hotkey.getKeybind(), null);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		buttonKeybind.onKeyPressed(keyCode);
		if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
			this.close();
			return true;
		} else if (this.commandList.keyPressed(keyCode, scanCode, modifiers))
			return true;
		else
			return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (buttonKeybind.onMouseClicked((int)mouseX, (int)mouseY, button)) return true;
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void close() {
		this.macro.actions.clear();
		this.commandList.children().forEach(t -> this.macro.actions.add(t.command));
		GuiBase.openGui(parent);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		buttonKeybind.render(context, mouseX, mouseY, buttonKeybind.isMouseOver(mouseX, mouseY));
	}
}
