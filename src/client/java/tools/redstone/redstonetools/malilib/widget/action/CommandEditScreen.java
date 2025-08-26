package tools.redstone.redstonetools.malilib.widget.action;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class CommandEditScreen extends Screen {

	private final TextFieldWidget commandField;
	private final ChatInputSuggestor commandSuggestor;
	private boolean changed = false;
	private final Screen parent;

	public CommandEditScreen(Screen parent, TextFieldWidget commandField) {
		super(Text.of(""));
		this.parent = parent;
		this.commandField = commandField;
		client = MinecraftClient.getInstance();
		this.commandSuggestor = new ChatInputSuggestor(client, this, commandField,client.textRenderer,true,false, commandField.getY() -20,5,false, -805306368) {
			@Override
			public void refresh() {
				if (client == null) return;
				if (client.getNetworkHandler() == null) return;
				super.refresh();
			}
		};

		commandField.setChangedListener((s) -> changed = true);
		commandSuggestor.setWindowActive(true);
		commandSuggestor.refresh();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		parent.render(context, mouseX, mouseY, delta);

		context.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

		commandField.render(context, mouseX, mouseY, delta);

		commandSuggestor.render(context, mouseX, mouseY);
		if (changed) {
			commandSuggestor.refresh();
			changed = false;
		}

		super.render(context, mouseX, mouseY, delta);

	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		parent.resize(client,width,height);
	}

	@Override
	public void close() {
		this.client.setScreen(parent);
		commandField.setFocused(false);
		commandField.setChangedListener(null);
		commandSuggestor.setWindowActive(false);
		commandSuggestor.refresh();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!commandField.mouseClicked(mouseX, mouseY, button)) {
			if (!commandSuggestor.mouseClicked(mouseX, mouseY, button)) {
				close();
			} else {
				commandField.setFocused(true);
			}
			return false;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount, double horzamount) {
		return commandSuggestor.mouseScrolled(amount);
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		return commandField.charTyped(chr,modifiers);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == InputUtil.GLFW_KEY_ESCAPE || keyCode == InputUtil.GLFW_KEY_ENTER || keyCode == InputUtil.GLFW_KEY_KP_ENTER) {
			close();
			return true;
		}
		commandSuggestor.keyPressed(keyCode, scanCode, modifiers);

		return commandField.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return commandField.keyReleased(keyCode, scanCode, modifiers);
	}
}
