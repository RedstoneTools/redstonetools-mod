package tools.redstone.redstonetools.malilib.widget.action;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
/*$ click_and_inputs_imports {*/



/*$}*/
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;

public class CommandEditScreen extends Screen {

	private final TextFieldWidget commandField;
	private final ChatInputSuggestor commandSuggester;
	private boolean changed = false;
	public final GuiMacroEditor parent;

	public CommandEditScreen(GuiMacroEditor parent, TextFieldWidget commandField) {
		super(Text.of(""));
		this.parent = parent;
		this.commandField = commandField;
		this.commandField.setMaxLength(256);
		client = MinecraftClient.getInstance();
		this.commandSuggester = new ChatInputSuggestor(client, this, commandField, client.textRenderer, false, false, commandField.getY() - 20, 5, false, -805306368) {
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
		parent.render(context, mouseX, mouseY, delta);
		commandField.render(context, mouseX, mouseY, delta);

		commandSuggester.render(context, mouseX, mouseY);
		if (changed) {
			commandSuggester.refresh();
			changed = false;
		}
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
	public boolean mouseClicked(/*$ click_params {*/int mouseX, int mouseY, int button/*$}*/) {
		if (!commandField.mouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/)) {
			if (!commandSuggester.mouseClicked(/*$ click_nodouble_args {*/mouseX, mouseY, button/*$}*/)) {
				close();
			} else {
				commandField.setFocused(true);
			}
			return false;
		}
		return super.mouseClicked(/*$ click_args {*/mouseX, mouseY, button/*$}*/);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount, double horizontalAmount) {
		return commandSuggester.mouseScrolled(amount);
	}

	@Override
	public boolean charTyped(/*$ charinput_params {*/char chr, int modifiers/*$}*/) {
		return commandField.charTyped(/*$ charinput_args {*/chr, modifiers/*$}*/);
	}

	@Override
	public boolean keyPressed(/*$ keyinput_params_vars2 {*/int keyCode, int scanCode, int modifiers) {/*$}*/
		if (keyCode == InputUtil.GLFW_KEY_ESCAPE || keyCode == InputUtil.GLFW_KEY_ENTER || keyCode == InputUtil.GLFW_KEY_KP_ENTER) {
			close();
			return true;
		}
		commandSuggester.keyPressed(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);

		return commandField.keyPressed(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
	}

	@Override
	public boolean keyReleased(/*$ keyinput_params {*/int keyCode, int scanCode, int modifiers/*$}*/) {
		return commandField.keyReleased(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
	}
}
