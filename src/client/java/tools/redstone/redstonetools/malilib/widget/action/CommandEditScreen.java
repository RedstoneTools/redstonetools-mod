package tools.redstone.redstonetools.malilib.widget.action;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;

public class CommandEditScreen extends Screen {

	private final TextFieldWidget commandField;
	private final ChatInputSuggestor commandSuggester;
	private boolean changed = false;
	private final GuiMacroEditor parent;

	public CommandEditScreen(GuiMacroEditor parent, TextFieldWidget commandField) {
		super(Text.of(""));
		this.parent = parent;
		this.commandField = commandField;
		client = MinecraftClient.getInstance();
		this.commandSuggester = new ChatInputSuggestor(client, this, commandField, client.textRenderer, true, false, commandField.getY() - 20, 5, false, -805306368) {
			@Override
			public void refresh() {
				if (client == null) return;
				if (client.getNetworkHandler() == null) return;
				super.refresh();
			}
		};

		commandField.setChangedListener((s) -> changed = true);
		commandSuggester.setWindowActive(true);
		commandSuggester.refresh();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {

		commandField.render(context, mouseX, mouseY, delta);

		commandSuggester.render(context, mouseX, mouseY);
		if (changed) {
			commandSuggester.refresh();
			changed = false;
		}
		super.render(context, mouseX, mouseY, delta);
		parent.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		parent.resize(client, width, height);
	}

	@Override
	public void close() {
		this.client.setScreen(parent);
		commandField.setFocused(false);
		commandField.setChangedListener(null);
		commandSuggester.setWindowActive(false);
		commandSuggester.refresh();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!commandField.mouseClicked(mouseX, mouseY, button)) {
			if (!commandSuggester.mouseClicked(mouseX, mouseY, button)) {
				close();
			} else {
				commandField.setFocused(true);
			}
			return false;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount, double horizontalAmount) {
		return commandSuggester.mouseScrolled(amount);
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		return commandField.charTyped(chr, modifiers);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == InputUtil.GLFW_KEY_ESCAPE || keyCode == InputUtil.GLFW_KEY_ENTER || keyCode == InputUtil.GLFW_KEY_KP_ENTER) {
			close();
			return true;
		}
		commandSuggester.keyPressed(keyCode, scanCode, modifiers);

		return commandField.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return commandField.keyReleased(keyCode, scanCode, modifiers);
	}
}
