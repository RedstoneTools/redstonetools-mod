package tools.redstone.redstonetools.malilib.widget.action;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
/*$ click_and_inputs_imports {*///
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.CharInput;/*$}*/
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
	public boolean mouseClicked(/*$ mouse_clicked_params {*/Click click, boolean doubleClick/*$}*/) {
		if (!commandField.mouseClicked(/*$ mouse_clicked_args {*/click, doubleClick/*$}*/)) {
			if (!commandSuggester.mouseClicked(/*? if >=1.21.10 {*/click/*?} else {*//*mouseX, mouseY, button*//*?}*/)) {
				close();
			} else {
				commandField.setFocused(true);
			}
			return false;
		}
		return super.mouseClicked(/*$ mouse_clicked_args {*/click, doubleClick/*$}*/);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount, double horizontalAmount) {
		return commandSuggester.mouseScrolled(amount);
	}

	@Override
	public boolean charTyped(/*$ charinput_params {*/CharInput input/*$}*/) {
		return commandField.charTyped(/*$ charinput_args {*/input/*$}*/);
	}

	@Override
	public boolean keyPressed(/*$ keyinput_params {*/KeyInput input/*$}*/) {
		//? if >=1.21.10 {
		int keyCode = input.key();
		//?}
		if (keyCode == InputUtil.GLFW_KEY_ESCAPE || keyCode == InputUtil.GLFW_KEY_ENTER || keyCode == InputUtil.GLFW_KEY_KP_ENTER) {
			close();
			return true;
		}
		commandSuggester.keyPressed(/*$ keyinput_args {*/input/*$}*/);

		return commandField.keyPressed(/*$ keyinput_args {*/input/*$}*/);
	}

	@Override
	public boolean keyReleased(/*$ keyinput_params {*/KeyInput input/*$}*/) {
		return commandField.keyReleased(/*$ keyinput_args {*/input/*$}*/);
	}
}
